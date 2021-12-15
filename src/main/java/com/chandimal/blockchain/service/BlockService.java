package com.chandimal.blockchain.service;

import com.chandimal.blockchain.dto.Block;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BlockService {

  @Value("${prefix}")
  private int prefix;

  @Value("${cores}")
  private byte NUMBER_OF_THREADS;

  private static List<Block> blockchain = new ArrayList<>();

  @PostConstruct
  public void mineGenisisBlock() {
    Block genesisBlock = new Block("The is the Genesis Block.", "0", new Date().getTime(),NUMBER_OF_THREADS);
    genesisBlock.mineBlock(prefix);
    blockchain.add(genesisBlock);
  }

}
