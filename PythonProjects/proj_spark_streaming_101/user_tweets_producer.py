from kafka import KafkaProducer
from random import randint
import time
from datetime import datetime

messages = [
    """Our country’s great First Lady, Melania, is doing really well in Africa. \
    The people love her, and she loves them! It is a beautiful thing to see.""",
    """Denis Mukwege and Nadia Murad’s work to end the use of sexual violence as a weapon of war has brought awareness \
    to a horrific practice and helped survivors get the care they need. My heartfelt congratulations to both \
    for their well-deserved Nobel Peace Prize.""",
    """Brett Kavanaugh has lied to the American people and lied to the Senate. \
    He has no place on the Supreme Court. """,
    """A verdict is reached in the trial of a Chicago police officer charged with murder \
    in the killing of 17-year-old Laquan McDonald""",
    """The unemployment rate fell to 3.7% in September, the lowest level since December 1969. \
    The US economy added 134,000 jobs last month. """,
    """Two protesters opposed to Brett Kavanaugh's Supreme Court nomination were arrested \
    on suspicion of blocking the entrance to Sen. Jeff Flake's office """,
    """I feel very upset""",
    """It is much, much worse to receive bad news through the written word than by somebody simply telling you, \
    and I’m sure you understand why. When somebody simply tells you bad news and that’s the end of it"""]

if __name__ == "__main__":

    rate = 10
    users = 10
    topic = "tweets_stream"
    bootstrap_servers = "localhost:9092,localhost:9093"

    # Create Kafka producer
    producer = KafkaProducer(bootstrap_servers=bootstrap_servers)

    # Initialization
    ts = time.time()

    # Send data
    while True:
        print("Sending data...")
        for i in range(rate):

            time_field = datetime.fromtimestamp(ts).strftime('%Y-%m-%d %H:%M:%S')
            ts = ts + randint(1, 10)
            message = messages[randint(0, len(messages) - 1)]
            user = "user-" + str(randint(1, users))
            producer.send(topic, key=bytes(user, "utf8"), value=bytes(message, "utf8"))

        time.sleep(1)
