package com.jifenke.lepluslive.web.rest;


import com.jifenke.lepluslive.Application;
import com.jifenke.lepluslive.global.config.Constants;


import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;


/**
 * Created by wcg on 16/4/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles({Constants.SPRING_PROFILE_DEVELOPMENT})
public class ttt {


  @Test
  public void tttt() {
    for (int i = 0; i < 100; i++) {
      int defaultScoreA = (new Random().nextInt(6) + 10) * 10;
      System.out.println(defaultScoreA);
    }
//    int x = 0, y = 0, z = 0;
//    for (int i = 0; i < 100000; i++) {
//      double random = Math.random();
//      if (random < 0.9) {
//        x++;
//      } else if (0.9 <= random && random < 0.97) {
//        y++;
//      } else {
//        z++;
//      }
//    }
//    System.out.println("x:" + x + "  y: " + y + "  z: " + z);

  }
}

////  public static void main(String[] args) {
////    int x[][] = new int[9][9];
////    for(int i=0;i<9;i++){
////      for(int y=0;y<9;y++){
////        x[i][y]=new Random().nextInt(2);
////      }
////    }
////    Scanner input = new Scanner(System.in);
////    int a = input.nextInt();
////    int b = input.nextInt();
////    int n = input.nextInt();
////
////    for(int z=1;z<n;z++){
////      int m = x[a][b];
////      int a1 = x[a-1][b];
////      int a2 = x[a+1][b];
////      int a3 = x[a][b+1];
////      int a4 = x[a][b-1];
////
////
////
////    }
//
//
//
//  }


