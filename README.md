# hkredis 

A high-performance, lightweight, in-memory key-value storage engine built in **pure Java 23** without external frameworks or dependencies. Fully compliant with the **Redis Serialization Protocol (RESP2)**.

---

## 🏗️ Architecture Overview

```text
                  +-----------------------------------+
                  |   Redis CLI / Client Application  |
                  +-----------------------------------+
                                    |
                            (RESP Protocol)
                                    v
                  +-----------------------------------+
                  |         Main (ServerSocket)       |
                  |     Port: 6379 / Custom Port      |
                  +-----------------------------------+
                                    |
                       (ExecutorService ThreadPool)
                                    v
                  +-----------------------------------+
                  |      RespParser & Tokenizer       |
                  +-----------------------------------+
                                    |
                    +---------------+---------------+
                    |                               |
             (Direct Mode)                   (Session Mode)
                    |                               |
                    v                               v
          +-------------------+           +-------------------+
          |  CommandRegistry  |           |   ClientContext   |
          +-------------------+           | (MULTI/EXEC Queue)|
                    |                     +-------------------+
                    v                               |
            Executable Command <--------------------+
                    |
      +-------------+-------------+-----------------+
      |                           |                 |
      v                           v                 v
+-----------+               +-----------+     +------------------+
|  DbStore  |               | AofManager|     | ReplicationState |
|(Concurrent|               |(Append-Only|     | (Role & Offsets) |
| HashMap)  |               | Persistence|     +------------------+
+-----------+               +-----------+


Features
RESP Protocol Parser: Native implementation of array (*) and bulk string ($) tokenizing over raw TCP byte streams.

Non-Blocking Multithreading: Thread-safe state handling powered by ExecutorService and ConcurrentHashMap.

Passive Key Expiration (TTL): Millisecond-precision key expiration support using EX and PX flags.

Atomic Transactions: Command-queuing mechanism implementing MULTI, EXEC, and DISCARD.

Append-Only File (AOF) Persistence: Automatic write-mutation logging and startup replay for crash recovery.

Replication Handshake: Native protocol compliance for INFO replication, PSYNC, and REPLCONF.

How to Build and Run
Prerequisites
Java 23 SDK

Apache Maven 3.9+

1. Build the Executable JAR


PowerShell
1. Build the Executable JAR
mvn clean package

2. Run the Server
Default port (6379):

PowerShell
java -jar target/hkredis-1.0-SNAPSHOT.jar

Custom port:

PowerShell
java -jar target/hkredis-1.0-SNAPSHOT.jar --port 30007
 Testing with redis-cli
You can connect directly using the official redis-cli or any standard Redis driver (Jedis, Lettuce, etc.):

Connect to Server
Bash
redis-cli -p 30007

Supported Commands & Examples
Basic Ping & Echo
Plaintext
127.0.0.1:30007> PING
PONG

127.0.0.1:30007> ECHO "Hello HK-Redis"
"Hello HK-Redis"
Key-Value Operations with Expiration (TTL)
Plaintext
127.0.0.1:30007> SET mykey "Hello World"
OK

127.0.0.1:30007> GET mykey
"Hello World"

# Set key with 10 seconds expiration
127.0.0.1:30007> SET tempkey "ExpiringData" EX 10
OK

127.0.0.1:30007> GET tempkey
"ExpiringData"
# (Wait 10 seconds)
127.0.0.1:30007> GET tempkey
(nil)
Transactions (MULTI / EXEC)
Plaintext
127.0.0.1:30007> MULTI
OK
127.0.0.1:30007(TX)> SET user "Hardesh"
QUEUED
127.0.0.1:30007(TX)> GET user
QUEUED
127.0.0.1:30007(TX)> EXEC
1) OK
2) "Hardesh"
Replication Info
Plaintext
127.0.0.1:30007> INFO replication
# Replication
role:master
master_replid:a1b2c3d4e5f678901234567890abcdef
master_repl_offset:0












