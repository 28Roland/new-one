package com.iii.data.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


class Node implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int digit;
    ArrayList<String> regionNames;
    Node[] links;

    Node(int digit) {
        this.digit = digit;
        regionNames = null;
        links = new Node[4];
    }

    public void addRegion(String regionName) {
        if (regionNames == null) {
            regionNames = new ArrayList<String>();
        }
        regionNames.add(regionName);
    }

    public Node addNode(int digit){
        Node child = new Node(digit);
        links[digit] = child;
        return child;
    }
}


public class QuadkeyTemplateDB implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Node root;
    Map<String, String> includedRegionsMap;
    boolean filterRegion = true;

    public QuadkeyTemplateDB(String csvFile) {
        root = new Node(-1);
        loadData(csvFile);
    }

    private void addQuadkey(String quadkey, String regionName) {
        Node cur = root;

        for (int i = 0; i < quadkey.length(); i++) {
            int digit = Integer.parseInt(quadkey.substring(i, i + 1));

            if (cur.links[digit] == null) {
                cur = cur.addNode(digit);
            } else {
                cur = cur.links[digit];
            }
        }
        cur.addRegion(regionName);
    }

    public void loadData(String csvFile) {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] row = line.split(cvsSplitBy);
                String regionName = row[0];
                String quadkey = row[2];

                addQuadkey(quadkey, regionName);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> lookupRegions(String quadkey) {
    	
    	if (quadkey == null) {
    		return new ArrayList<String>(); //return []
    	}
    	
        Node cur = root;
        List<String> regionFound = new ArrayList<String>();

        for (int i = 0; i < quadkey.length(); i++) {
            int digit = Integer.parseInt(quadkey.substring(i, i + 1));
            
            if (cur.links[digit] != null) {
                cur = cur.links[digit];
                if (cur.regionNames != null) {
                    regionFound.addAll(cur.regionNames);
                }
            } else {
                break;
            }
        }
        return regionFound;
    }

    public static void main(String[] args) {

        // create db
        long lStartTime = System.currentTimeMillis();
        QuadkeyTemplateDB db = new QuadkeyTemplateDB("/home/cloudera/Desktop/hive_in_practice/audi/region_template/qk_cn.csv");

        long lEndTime = System.currentTimeMillis();

        long difference = lEndTime - lStartTime;
        System.out.println("Elapsed milliseconds (create tree): " + difference +  " ms.");

        // start time
        lStartTime = System.currentTimeMillis();
        int round = 1;

        for (int i = 0; i < round ; i++) {
            List<String> found = db.lookupRegions("132100103322203");    // 13212321121330012,03023203030232031
            System.out.println(found);
        }

        // end time
        lEndTime = System.currentTimeMillis();
        difference = lEndTime - lStartTime;
        System.out.println("Elapsed milliseconds (lookup): " + difference +  " ms. " + "rounds: " + round);
        System.out.println("Milliseconds for each lookup: " + ((double)difference/(double)round) + " ms.");

    }
}