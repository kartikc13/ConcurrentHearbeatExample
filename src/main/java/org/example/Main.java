package org.example;

import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        String result = null;
        try {
            int j = 1;
            CompletableFuture<String> heartBeatFuture = startActivityExecution();
            while (!heartBeatFuture.isDone()){
                System.out.println("*******HEARTBEATING DONE " + j++ + "*********");
                try {
                    if (j == 5) {
                        throw new ActivityCompletionException(); //simulating the temporal's hearbeat exception
                    }
                    Thread.sleep(2000L);
                } catch (ActivityCompletionException e) {
                    heartBeatFuture.completeExceptionally(e);   //kill the activity main logic execution
                    System.out.println("Activity Completion Exception occurred!");
                    return;     //instead throw the ActivityCompletionException here as 'throw e;'
                }
            }
            result = heartBeatFuture.get(); //get the final worklist filepath if no termination
        } catch (Exception e) {
            System.out.println("Runtime Exception occurred!");
//            throw e.getCause();
        }
//        return result;
    }

    private static CompletableFuture<String> startActivityExecution(){
        return CompletableFuture.supplyAsync(() -> {
            int i = 1;
            while (i <= 10){
                try {
                    Thread.sleep(2000L);
                    System.out.println("*******MAIN CODE EXECUTION DONE " + i++ + "*********");
                } catch (InterruptedException e) {
                    System.out.println("Thread sleep failed!");
                    throw new RuntimeException(e);
                }
            }
            return "Success";
        });
    }
}