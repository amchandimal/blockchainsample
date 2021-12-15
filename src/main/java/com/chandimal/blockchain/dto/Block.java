package com.chandimal.blockchain.dto;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;

public class Block {

  private boolean hashCalculated = false;
  private int[] nonceArray;
  private int NUMBER_OF_THREADS = 8;

  private String hash;
  private String previousHash;
  private String data;
  private long timeStamp;
  private int nonce;

  private void setHashCalculatedTrue(int tempNonce,String tempHash){
    synchronized (Block.class) {
      if (!this.hashCalculated) {
        hashCalculated = true;
        nonce = tempNonce;
        hash = tempHash;
        System.out.println("Correct Hash: " + tempHash);
        System.out.println("Correct Nonce: " + nonce);
      }
    }
  }


  public Block(String data, String previousHash, long timeStamp,int numberOfThreads) {
    this.data = data;
    this.previousHash = previousHash;
    this.timeStamp = timeStamp;
    this.hash = calculateBlockHash();
    NUMBER_OF_THREADS = numberOfThreads;
    this.nonceArray = new int[NUMBER_OF_THREADS];
  }

  public String calculateBlockHash(){
     return calculateBlockHash(nonce);
  }

  public String calculateBlockHash(int nonce) {
    String dataToHash = previousHash
        + timeStamp
        + nonce
        + data;
    MessageDigest digest = null;
    byte[] bytes = null;
    try {
      digest = MessageDigest.getInstance("SHA-256");
      bytes = digest.digest(dataToHash.getBytes(UTF_8));
    } catch (NoSuchAlgorithmException ex) {
      ex.printStackTrace();
    }
    StringBuffer buffer = new StringBuffer();
    for (byte b : bytes) {
      buffer.append(String.format("%02x", b));
    }
    return buffer.toString();
  }

  private void mine(int prefix,int currentThread) {
    System.out.println("Mining in Thread "+ currentThread);
    long startTime = new Date().getTime();
    String prefixString = new String(new char[prefix]).replace('\0', '0');
    String tempHash = hash;
    nonceArray[currentThread] = (Integer.MAX_VALUE / NUMBER_OF_THREADS) * currentThread;
    while (!tempHash.substring(0, prefix)
        .equals(prefixString)) {
      if(hashCalculated){
        return;
      }
      nonceArray[currentThread]++;
      tempHash = calculateBlockHash(nonceArray[currentThread]);
    }
    setHashCalculatedTrue(nonceArray[currentThread],tempHash);
    System.out.println("Time it took: " + (new Date().getTime() - startTime) + " ms");
  }

  public String mineBlock(final int prefix) {
    for(int i = 0; i< NUMBER_OF_THREADS; i++){
      final int currentThread = i;
      new Thread(new Runnable() {
        @Override
        public void run() {
          mine(prefix,currentThread);
        }
      }).start();
    }
    while (!hashCalculated){
      try {
        System.out.println(nonceArray[0] * NUMBER_OF_THREADS + " Checked");
        Thread.sleep(1000);
      }catch (Exception e){
        e.printStackTrace();
      }
    }
    return hash;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public String getPreviousHash() {
    return previousHash;
  }

  public void setPreviousHash(String previousHash) {
    this.previousHash = previousHash;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

  public int getNonce() {
    return nonce;
  }

  public void setNonce(int nonce) {
    this.nonce = nonce;
  }
}
