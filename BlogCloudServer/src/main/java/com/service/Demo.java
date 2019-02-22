package com.service;

/**
 * @author zhanghaijun
 */
public class Demo {

    public static void main(String[] args) {
        Puzzle4b[] obs = new Puzzle4b[6];
        int y = 1;
        int x = 0;
        int re = 0;
        while (x < 6) {
            obs[x] = new Puzzle4b();
            obs[x].ivar = y;
            y = y * 10;
            x = x + 1;
        }
        x = 6;
        while (x > 0) {
            x = x - 1;
            re = re + obs[x].doStuff(x);
        }
        System.out.println("com.service.Demo.main()" + re);
    }
}

class Puzzle4b {

    int ivar;

    public int doStuff(int n) {
        if (n > 100) {
            return ivar * n;
        } else {
            return ivar * (5 - n);
        }
    }
}
