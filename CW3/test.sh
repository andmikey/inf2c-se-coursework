cd AuctionHouse
export CLASSPATH=bin:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar
javac -sourcepath src -d bin src/auctionhouse/*.java
java auctionhouse.AllTests
