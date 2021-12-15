package com.chandimal.blockchain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.chandimal.blockchain.dto.Block;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BlockChainTest {

  private static List<Block> blockchain = new ArrayList<>();
  private static int prefix = 5;
  private static String prefixString = new String(new char[prefix]).replace('\0', '0');

  @BeforeClass
  public static void setUp() {
    Block genesisBlock = new Block("The is the Genesis Block.", "0", new Date().getTime(),8);
    genesisBlock.mineBlock(prefix);
    blockchain.add(genesisBlock);
    Block firstBlock = new Block("The is the First Block.", genesisBlock.getHash(), new Date().getTime(),8);
    firstBlock.mineBlock(prefix);
    blockchain.add(firstBlock);
  }
  @Test
  public void givenBlockchain_whenValidated_thenSuccess() {
    boolean flag = true;
    for (int i = 0; i < blockchain.size(); i++) {
      String previousHash = i==0 ? "0" : blockchain.get(i - 1).getHash();
      flag = blockchain.get(i).getHash().equals(blockchain.get(i).calculateBlockHash())
          && previousHash.equals(blockchain.get(i).getPreviousHash())
          && blockchain.get(i).getHash().substring(0, prefix).equals(prefixString);
      if (!flag) break;
    }
    assertTrue(flag);
  }

}
