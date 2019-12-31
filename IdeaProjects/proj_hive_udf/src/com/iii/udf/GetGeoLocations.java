package com.iii.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.Text;

import com.iii.data.utils.QuadkeyTemplateDB;

import java.util.*;

public final class GetGeoLocations extends UDF {

	private QuadkeyTemplateDB qkDB;

	public Text evaluate(Text quadkey) {
		if (quadkey == null) {
			return null;
		}
		
		if (qkDB == null) {
			qkDB = new QuadkeyTemplateDB("./qk_cn.csv");
		}
		
		StringBuffer regionsFoundSB = new StringBuffer();
		List<String> regionsFound = null;
		
		regionsFound = qkDB.lookupRegions(quadkey.toString());
		
		for (int i = 0 ; i < regionsFound.size(); i++) {
			if (i == regionsFound.size() - 1) {
				regionsFoundSB.append(regionsFound.get(i)); 
			} else {
				regionsFoundSB.append(regionsFound.get(i)).append(",");
			}
		}
		return new Text(regionsFoundSB.toString());
	}

}
