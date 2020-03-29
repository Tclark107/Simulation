//============================================================================================================================
// Simulation.java
// This program will simulate shoppers waiting in a Queue.
// Tristan Clark
//============================================================================================================================

import java.io.*;
import java.util.Scanner;

import javax.print.attribute.standard.Finishings;

public class Simulation { 

    static void Usage() {
        System.err.println("Usage: java Simulation input_file");
        System.exit(0);
    }

    public static Job getJob(Scanner in) {
        String[] s = in.nextLine().split(" ");
        int a = Integer.parseInt(s[0]);
        int d = Integer.parseInt(s[1]);
        return new Job(a, d);
     }

    public static void main(String args[]) throws IOException {
        if(args.length!=1){
            Usage();
        }
        
        Scanner in = new Scanner(new File(args[0]));
        int i;

        // number of jobs
        int m = Integer.parseInt(in.nextLine());
        System.out.println("m: " + m);

        // stores the list of Jobs 
        Job[] jobQueue = new Job[m];
        for(i = 0; i < m; i++) jobQueue[i] = getJob(in);
        // keeps track of the jobs processed
        int jobQueueCount;
        //for(i = 0; i < m; i++) System.out.println(jobQueue[i]);

        int minutesInADay = 1440;
        Queue jobsCompleted = new Queue();
        int processorsActive = 0;
        int minLength = 0;
        int nextFinishTime = 0;
        int arrivalTime = 0;
        int time = 0;
        int temp = 1;
        boolean sameTime = false;

        // Finish time variables.
        // This is an array of Finish times for each processor
        int[][] finishTimesForProcessor;
        // This is how many finish times are currently in said array. 
        int[] finishTimeCount;
        // This keeps track of which finish time is currently being processed.
        int[] currentFinishTime;

        System.out.println("Trace file: arg[0].trc" );
        System.out.println(m + " Jobs:");
        for(int iter = 0; iter < m; iter++) System.out.print(jobQueue[iter]);
        System.out.println();

        // process m jobs with n processor Queues
        for(int n = 1; n < m; n++) {
            System.out.println("*********************");
            System.out.println(n + " procesor:");
            System.out.println("*********************");


            // initialize processor Queues
            Queue[] processor = new Queue[n];
            for(i = 0; i < n; i++) processor[i] = new Queue();

            // initialize finish time variables
            finishTimesForProcessor = new int[n][m+1];
            finishTimeCount = new int[m];
            currentFinishTime = new int[m];

            // resets jobs processed
            jobQueueCount = 0;
            
            // while there is still information in input or processors
            // still waiting/being processed continue to process 
            while(jobQueueCount < m || processorsActive > 0) {
            //while(jobQueueCount < m) {
                //System.out.println("still processing");

                // process each job at processor[i]
                for(i = 0; i < n; i++) {
                    System.out.println("in processor " + i);

                    // make sure you update the minimum length
                    minLength = Math.min(minLength,processor[i].length());
                    
                    // check if nextFinish time is 0
                    if(nextFinishTime == 0) {

                        // if there are more jobs then make it large and time will take the min
                        if(jobQueueCount < m) {
                            nextFinishTime = minutesInADay;
                            //System.out.println("nFT is 0 and there is another line");

                        // otherwise make the next finish time the current finish time for this processor
                        // then check if thats 0 and if it is move on to the next processor
                        } else {
                            //System.out.println("nFT is 0 and there is not another line");
                            //System.out.println("fTfp[i][cft[i]]" + finishTimesForProcessor[i][currentFinishTime[i]]);
                            nextFinishTime = finishTimesForProcessor[i][currentFinishTime[i]];
                            if(nextFinishTime == 0) continue;
                        }
                    }

                    // if nextFinishTime != 0 and there are more jobs
                    // time is equal to the minimum of the new job and the nextFinishTime
                    if(jobQueueCount < m) { 
                        //System.out.println("nFT is !0 and there is another line");
                        arrivalTime = jobQueue[jobQueueCount].getArrival();
                        System.out.println("aT: " + arrivalTime);
                        System.out.println("nFT: " + nextFinishTime);
                        temp = Math.min(arrivalTime,nextFinishTime);

                    // if nextFinishTime != 0 and there are no more jobs
                    // finish processing the jobs
                    } else {
                        //System.out.println("jobQueueCount " +jobQueueCount);
                        //System.out.println("nFT is !0 and there is not another line");
                        temp = nextFinishTime;
                    }

                    // check if arrivalTime and nextFinishTime are the same
                    // if they are make sure to increment minLength in enqueue
                    if(finishTimesForProcessor[i][currentFinishTime[i]] == arrivalTime) {
                        sameTime = true;
                    }

                    //System.out.println("temp: " +temp);
                    //System.out.println("teim: " +time);
                    //System.out.println("i: " + i);
                    //System.out.println("nft: " +nextFinishTime);
                    // if time is updated and we aren't on the first processor
                    if(temp != time) {
                        if(i!=0) {
                            continue;
                        }
                        time = temp;
                    }
                    System.out.println("time=" + time);
                    System.out.print("0: ");
                    for(int iter = 0; iter < m; iter++) System.out.print(jobQueue[iter]);
                    System.out.println(jobsCompleted);
                    for(int iter = 0; iter <n; iter++) {
                        System.out.print((iter+1) + ": ");
                        System.out.println(processor[iter]);
                    }
                    //System.out.println("nFT: ");

                    // catch if arrivalTime is a weird large number
                    if(time == minutesInADay) {
                        System.err.println("time is equal to max allowed time");
                        System.exit(1);
                    } 

                    // Dequeue process
                    //System.out.println("nFT before dequeu: " + nextFinishTime);
                    //System.out.println("time before dequeu: " + time);
                    //if(nextFinishTime == time) {
                    System.out.println("b4");
                    for(int j = 0; j < m; j++) System.out.println(finishTimesForProcessor[i][j]);
                    System.out.println("b4");
                    System.out.println("current finish time"+finishTimesForProcessor[i][currentFinishTime[i]]);
                    if(finishTimesForProcessor[i][currentFinishTime[i]] == time) {
                        System.out.println("processor: " + (i+1));
                        System.out.println("current nFT == time");
                        currentFinishTime[i] = currentFinishTime[i]+1;
                        //System.out.println("currentFinishtime++ " + currentFinishTime[i]);
                        nextFinishTime = finishTimesForProcessor[i][currentFinishTime[i]];
                        //System.out.println("cft++ " + finishTimesForProcessor[i][currentFinishTime[i]++]);
                        //System.out.println("cft " + finishTimesForProcessor[i][currentFinishTime[i]]++);
                        System.out.println("nft " + nextFinishTime);
                        //System.out.println("b4");
                        //for(int j = 0; j < m; j++) System.out.println(finishTimesForProcessor[i][j]);
                        //System.out.println("b4");

                        // compare the next finish time with the other arrays finish times
                        for(int j = 0; j < n; j++) {
                            if(nextFinishTime == 0){
                                 nextFinishTime = finishTimesForProcessor[j][currentFinishTime[j]];
                                 //System.out.println("if nft is equal to 0 " + nextFinishTime);
                            }
                            if(j!=i) {
                                if(finishTimesForProcessor[j][currentFinishTime[j]] > 0) {
                                    nextFinishTime = Math.min(nextFinishTime, finishTimesForProcessor[j][currentFinishTime[j]]);
                                }
                            }
                            //System.out.println("nft after min" + nextFinishTime);
                        }
                        System.out.println("nFT: " + nextFinishTime);
                        if(processor[i].length() == minLength) minLength--;
                        jobsCompleted.enqueue(processor[i].dequeue());
                        processorsActive--; 
                    }

                    // Enqueue process
                    //System.out.println("arrival time before enque" + arrivalTime);
                    //System.out.println("time before enque" + time);
                    //System.out.println(arrivalTime == time);
                    if(arrivalTime == time) {
                        System.out.println("aT == time");
                        System.out.println("processor: " + (i+1));
                        System.out.println("mL: " + minLength);
                        System.out.println("processsor length: " + processor[i].length());

                        if(processor[i].length() == minLength) {
                            //System.out.println("p[" + i + "].length() == mL");
                            
                            // if the queue is !empty then add the new finish time to the current finish time                                //System.out.println("fTC: "+finishTimeCount[i]);

                            //System.out.println("fTC: "+finishTimeCount[i]);
                            // if the processor queue is not empty
                            if(!processor[i].isEmpty()) {
                                System.out.println("processor is not empty");
                                //System.out.println(finishTimeCount[i]);
                                //System.out.println("finTimeCount[" + i +"] > 0");
                                //for(int j = 0; j < m; j++) System.out.println(finishTimesForProcessor[i][j]);
                                //System.out.println("jQC: " + jobQueueCount);
                                //System.out.println("i: " + i);
                                //System.out.println("fTC: "+finishTimeCount[i]);
                                //System.out.println("fTC: "+ (finishTimeCount[i]-1));
                                jobQueue[jobQueueCount].computeFinishTime(finishTimesForProcessor[i][finishTimeCount[i]-1]);
                                finishTimesForProcessor[i][finishTimeCount[i]] = jobQueue[jobQueueCount].getFinish();
                                //System.out.println(finishTimesForProcessor[i][finishTimeCount[i]]);

                            // otherwise make the new finish time the current finish time
                            } else {
                                System.out.println("else");
                                jobQueue[jobQueueCount].computeFinishTime(time);
                                finishTimesForProcessor[i][finishTimeCount[i]] = jobQueue[jobQueueCount].getFinish();
                            }
                            //System.out.println("ftfp[" + i + "][" + finishTimeCount[i] + "] = " + finishTimesForProcessor[i][finishTimeCount[i]] );
                            finishTimeCount[i]++;
                            processor[i].enqueue(jobQueue[jobQueueCount]);
                            //System.out.println("nFT before min" + nextFinishTime);
                            nextFinishTime = Math.min(nextFinishTime, finishTimesForProcessor[i][currentFinishTime[i]]);
                            //System.out.println("nFT after min" + nextFinishTime);
                            System.out.println("i: " + i);
                            System.out.println("mL: " + minLength);
                            if(i == n-1 || sameTime) {
                                minLength++; // had an error if both enqueue and dequeue happened at the same time
                                sameTime = false;
                            }
                            processorsActive++;
                            jobQueueCount++;
                        }
                    } // end if
                    //System.out.println("jobQCount: "+ jobQueueCount);
                    //System.out.println("pA: "+ processorsActive);
                } // end for
                //if(jobQueueCount == 1) break;
            } // end  while

            System.out.println("complete queue: " + jobsCompleted);

            for(int iter = 0; iter < n; iter++) {
            //    processor[iter].dequeueAll();
                System.out.print((iter+1) + ": ");
                System.out.println(processor[iter]);
            }
            System.out.print("completed: ");
            jobsCompleted.dequeueAll();
            System.out.println(jobsCompleted);
            for(int iter = 0; iter < m; iter++) {
                jobQueue[iter].resetFinishTime();
                System.out.print(jobQueue[iter] + " ");
            }
            System.out.println();
            System.out.println("start " + (n+1) + "  processor");
            
        } // end for
        System.out.println(jobsCompleted);

            // Because my while loop depends on a counter
            // I process the first element so that the 
            /*if(in.hasNextLine()){
                J = getJob(in);
                time = J.getArrival();
                J.computeFinishTime(time);
                nextFinishTime = J.getFinish();
                finishTimesForProcessor[0][currentFinishTime[0]] = nextFinishTime;
                currentFinishTime[0]++;
                processor[0].enqueue(J);
                if(processor.length == 1) minLength++;
                processorsActive++;
            }*/

        /*int time = 0;
        int nextArrivalTime;
        int nextFinishTime = 1000;
        int minLength = 0;
        int currentFinishTime;
        System.out.println(m);
        
        Job[] storage = new Job[m];
        for(int i = 0; i < m; i++) storage[i] = (getJob(in));
        //for(int i = 0; i < m; i++) System.out.println(storage[i]);
        System.out.println(storage[0].getArrival());

        Queue Q = new Queue();
        //for(int i = 0; i < m; i++) Q.enqueue(storage[i]);
        //System.out.println(Q);

        // processor for loop
        int Atime = -1;
        int count = 0;
        for(int n = 1; n < m; n++) {
            Queue[] processor = new Queue[n]; 
            //int[] FinishTime = new int[n+1];
            //System.out.println(storage[0].getArrival());

            // initialize the processor Queues
            for(int i = 0; i < n; i++) processor[i] = new Queue();

            for(int i = 0; i<=n; i++) {
                processor[0].enqueue(storage[i]);
            }
            System.out.println(processor[0]);

            // testing 
            //nextArrivalTime = storage[++Atime].getArrival();
            //System.out.println("nAT: " + nextArrivalTime);
            //time = Math.min(nextArrivalTime,nextFinishTime);
            //System.out.println("time: " + time);


            // maybe for (i = 0 to m-1) processors[0].enqueue(storage[i]);

            //probably delete this nextFinishTime = storage[0].getArrival());
            //while(true) {
            //  nextArrivalTime = storage[Atime++].getArrival());
            //  time = min(nextArrivalTime,nextFinishTime);
            //  for(int i = 1; i <= n; i++) {
            //    if(nextFinishTime == time) {
            //      processors[0].enqueue(storage[tag]);
            //      storage[tag].finish = nextFinishtime;
            //      processors[i].dequeue();
            //      nextFinishTime = min(nextFinishTime, processors[i].peek().computeFinishtime(time));
            //      minLength = min(minLength, processors[i].length());
            //    }
            //    if(nextArrivalTime == time) {
            //      if(minLength == processor[i].length()) {
            //          processor[0].dequeue();
            //          processor[i].enqueue(storage[tag]); //what if tag = Atime??
            //          // get rif of this?? nextFinishTime = min(storage[tag].computefinishtime(time), nextFinishtime);
            //          minLength = min(minLength,processor[i].length());
            //          if(i == n) minLength++;
            //      }
            //    }
            //  }
            //  nextArrivalTime = storage[Atime++].getArrival(); //will create an error as Atime+ goes over storage capacity
                                                                // might be something here with iterating through storage
                                                                // and the way that I might tag these items
            //  //maybe dont need thesenextFinishtime = min(nextfinishtime, currentfinishtime)
            //  //maybe dont need theseminLength = min(minLength, currentLength)
            //  if(count == 0) break;
            //}
        }*/

    } // end main
}