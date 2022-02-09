package com.pavell.rickAndMortyApi.backup;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BackupService {
    public void doBackup() throws IOException {
    }

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 1000; i++) {
            Resource resource = new Resource();
            resource.i = 5;

            MyTread myTread = new MyTread();
            myTread.setName("one");
            MyTread myTread2 = new MyTread();

            myTread.setResource(resource);
            myTread2.setResource(resource);

            myTread.start();
            myTread2.start();

            myTread.join();
            myTread2.join();

            System.out.println(resource.i);
        }



    }

}

class MyTread extends Thread {
    Resource resource;

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        resource.changeI();
    }
}

class Resource {
    int i;

    public  synchronized void changeI() {
        int i = this.i;
        if (Thread.currentThread().getName().equals("one")){
            Thread.yield();
        }
        i++;
        this.i = i;
    }
}

class MyTread2 extends Thread {
    @Override
    public   void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " MyTread2 with iteration " + i);
        }
        System.out.println(Thread.currentThread().getName() + " MyTread2 finished ");
    }
}