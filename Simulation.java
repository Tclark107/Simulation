//-----------------------------------------------------------------------------------------------------------
// Simulataion.java
// This is my second attempt at writing this algorithm.
// The first one was a mess
// Tristan Clark
//-----------------------------------------------------------------------------------------------------------

import java.io.*;
import java.util.Scanner;
import java.util.Arrays;

class Simulation {

    // if input is wrong print an error
    static void Usage() {
        System.err.println("Usage: java Simulation input_file");
        System.exit(0);
    }

    // helper function that initializes backUp Job Array
    public static Job getJob(Scanner in) {
        String[] s = in.nextLine().split(" ");
        int a = Integer.parseInt(s[0]);
        int d = Integer.parseInt(s[1]);
        return new Job(a, d);
     }

    // resets the backUp[] job array and the storage Queue that we manipulate the other Queues with
    static void resetBackUp(int numOfJobs, Job[] arrayOfJobs, Queue storageQ) {
        storageQ.dequeueAll();
        for(int i = 0; i < numOfJobs; i++) {
            arrayOfJobs[i].resetFinishTime();
            storageQ.enqueue(arrayOfJobs[i]);
        }
    }

    // helper function to print integer Array
    static void printArr(int[] arr) {
        for(int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        System.out.println();
    }

    // helper function for insertion sort
    // shifts the finish times over one to make room for the new finish time 
    static void shift(int[] finishTimes,int newStart, int end) {
        for (int i = end; i >= newStart; i--) {
            finishTimes[i+1] = finishTimes[i];
        }
    }

    // sort the finish times in order so they are easy to access
    static int iSort(int[] finishTimes,int start, int end, int newItem) {
        for (int i = start; i <= end; i++) {
            if(finishTimes[i] == newItem) {
                return end;
            }
            if(finishTimes[i] > newItem) {
                shift(finishTimes, i, end);
                finishTimes[i] = newItem;
                return ++end;
            }
        }
        if(finishTimes[end] < newItem){
            finishTimes[end+1] = newItem;
            return ++end;
        }
        return end;
    }

    // helper function that prints the current processor
    static void printProcessor(Job[][] jobArr, int processor) {
        System.out.print("This processor has the values: ");
        for(int i = 0; i < jobArr[processor].length; i++) {
            System.out.print(jobArr[processor][i]+ ", ");
        }
        System.out.println();
    }

    // moves the jobCursor to the next element 
    static int processFinishTime(Job[][] jobArr, int processor, int currJob, int currentDTime) {
        jobArr[processor][currJob].computeFinishTime(currentDTime);
        return jobArr[processor][currJob].getFinish();
    }

    // helper function that prints the job Array because I keep having to do it
    static void printJobArray(Job[] jobArray, int numJobs, PrintWriter myout) {
        //trace.println("job array :");
        for(int i = 0; i < numJobs; i++) myout.print(jobArray[i]);
        myout.println();
    }

    // helper function that prints Queue
    static void printQueue(Queue processor) {
        System.out.println("Queue:");
        System.out.println(processor);
    }

    public static void main(String[] args) throws IOException{
        
        // if user input is wrong print an err
        if(args.length!=1){
            Usage();
        }
        
        // initialize out files
        Scanner in = new Scanner(new File(args[0]));
        PrintWriter report = new PrintWriter(new FileWriter(args[0]+".rpt"));
        PrintWriter trace = new PrintWriter(new FileWriter(args[0] + ".trc"));
        trace.println("Trace file: " + args[0] + ".trc");
        report.println("Report file: " + args[0] + ".rpt");

        // number of jobs
        int m = Integer.parseInt(in.nextLine());
        trace.println(m + " Jobs:");
        report.println(m + " Jobs:");

        // initialize a backup array with the jobs
        Job[] backUp = new Job[m];
        int backUpCounter;
        for(int i = 0; i < m; i++) backUp[i] = getJob(in);
        printJobArray(backUp, m, trace);
        printJobArray(backUp, m, report);
        trace.println();

        // print report stars
        report.println();
        report.println("***********************************************************");

        // initialize the storage Queue that we will use to keep track of jobs left and Active
        int jobsActive;
        int jobsLeft;
        Queue storage = new Queue();
        for(int i = 0; i < m; i++) storage.enqueue(backUp[i]); 
        //trace.println(storage);

        // keep track of jobs in each processor and 
        // a cursor to know where we are
        // a counter to know how many elements are left
        Job[][] jobArray;
        int[] jobCursor;
        int[] jobArrayCounter;

        // keep track of sorted array of finish times
        // and what the current finish time is and which one we are working with
        // and an array for each processor that will keep track of that processors departure times
        int[] finishTimes = new int[m+1];
        int fTimeCurr;
        int fTimeCounter;
        int[] dTime;
        

        // keeps track of the current arrival time
        int arrivalTime;

        // keeps track of the smallest queue and how many there are
        int minLength;
        int minLengthCounter;

        // for comparing to time and the current time
        // a breakFlag for if the timechanges during arrivals
        int temp;
        int time;
        boolean breakFlag = false;
        boolean printFlag;

        // for processing the wait times
        int totalWaitTime;
        float avgWaitTime;
        int maxWaitTime;
        int currentWaitTime;

        // run the program on n processor Queues
        for(int n = 1; n < m; n++) {
            trace.println("*****************************");
            if(n==1) {
                trace.println(n + " processor:");
            } else {
                trace.println(n + " processors:");
            }
            trace.println("*****************************");

            // initialize the while-conditions and time
            jobsLeft = m;
            jobsActive = 0;
            time = 0;

            // initialize the Counter and Current Finish Times
            // initialize dTime have "n" slots
            // set finish time at 0 to be minutes in a day so that it chooses the smaller
            finishTimes[0] = 1440;
            fTimeCurr = 0;
            fTimeCounter = 0;
            dTime = new int[n];

            // initialize backUpCounter
            backUpCounter = 0;

            // initialize arrivalTime and FinishTime
            arrivalTime = backUp[backUpCounter].getArrival();
            temp = 0;

            // initialize mL and mLC
            minLength = 0;
            minLengthCounter = n;

            // reset waittime
            totalWaitTime = 0;
            maxWaitTime = 0;

            // initialize processor Queues and the job Array, cursor and counter
            Queue[] processor = new Queue[n];
            for(int i = 0; i < n; i++) processor[i] = new Queue();
            jobArray = new Job[n][m];
            jobCursor = new int[n];
            jobArrayCounter = new int[n];
            Arrays.fill(jobCursor,1);
            
            printFlag = true;

            // print the processors
            trace.println("time = " + time);
            if(jobsLeft == m && jobsActive ==0) {
                trace.println("0: "+ storage);
                for(int i = 0; i < n; i++) {
                    trace.println((i+1) + ": " + processor[i]);
                }
                trace.println();
            }

            // while there are jobsLeft and processors still processing jobs, continue
            while(jobsLeft != 0 || jobsActive != 0) {

                // loop through the processor queues do all jobs at time
                // process departures at time
                if(finishTimes[fTimeCurr] == time) {
                    for (int i = 0; i < n; i++) {
                        if(dTime[i] == time) {
                            if(!processor[i].isEmpty()) {
                                if(processor[i].length() == minLength) {
                                    minLength = processor[i].length()-1;
                                    minLengthCounter = 0;
                                }
                                storage.enqueue(processor[i].dequeue());
                                jobsActive--;
                                if(!processor[i].isEmpty()) {
                                    dTime[i] = processFinishTime(jobArray, i, jobCursor[i], dTime[i]); 
                                    fTimeCounter = iSort(finishTimes, fTimeCurr, fTimeCounter, dTime[i]);
                                    currentWaitTime = jobArray[i][jobCursor[i]].getWaitTime();
                                    totalWaitTime += currentWaitTime;
                                    maxWaitTime = Math.max(maxWaitTime,currentWaitTime);
                                } else {
                                    dTime[i] = 1440;
                                }
                                jobCursor[i]++;
                            }
                            if(processor[i].length() == minLength) {
                                minLengthCounter++;
                            }
                        } 
                    }
                    fTimeCurr++;
                    printFlag = true;
                } // end if

                // process arrivals at time
                if (arrivalTime == time) {
                    for(int i = 0; i < n; i++) {
                        jobArray[i][jobArrayCounter[i]] = backUp[backUpCounter];
                        if(processor[i].length() == minLength) {
                            minLengthCounter--;
                            if(processor[i].isEmpty()) {
                                dTime[i] = processFinishTime(jobArray, i, jobCursor[i]-1, arrivalTime);
                                fTimeCounter = iSort(finishTimes, fTimeCurr, fTimeCounter, dTime[i]);
                                processor[i].enqueue(storage.dequeue());
                                currentWaitTime = jobArray[i][jobCursor[i]-1].getWaitTime();
                                totalWaitTime += currentWaitTime;
                                maxWaitTime = Math.max(maxWaitTime,currentWaitTime);
                            } else {
                                processor[i].enqueue(storage.dequeue());
                            }
                            jobsActive++;
                            jobsLeft--;
                            jobArrayCounter[i]++;

                            // update arrival time and time
                            if(backUpCounter < m-1)temp = backUp[backUpCounter+1].getArrival();
                            if(jobsLeft == 0) temp = 0;
                            backUpCounter++;
                            if(temp != arrivalTime) {
                                arrivalTime = temp;
                                breakFlag = true;
                                printFlag = true;
                            }
                        }

                        // if minLengthCounter is 0, loop through the processors and count the processors at mL
                        if(minLengthCounter == 0) {
                            minLength = processor[i].length();
                            for(int j = 0; j < n; j++) {
                                if(processor[j].length() == minLength) minLengthCounter++;
                            }
                            breakFlag = true;
                        }

                        if(breakFlag) {
                            breakFlag = false;
                            break;
                        }
                    } // end for
                } // end if

                // if the time changed  and there are still jobs Active, print
                if(printFlag && jobsActive != 0 && jobsLeft != m) {
                    trace.println("0: "+ storage);
                    for(int i = 0; i < n; i++) {
                        trace.println((i+1) + ": " + processor[i]);
                    }
                    trace.println();
                }

                // process next time
                if(arrivalTime != 0) {
                    time = Math.min(finishTimes[fTimeCurr], arrivalTime);
                } else {
                    time = finishTimes[fTimeCurr];
                }

                // print the processors
                if(printFlag && time < 1440) {
                    trace.println("time = " + time);
                    printFlag = false;
                }

            } // end while */

            // print the last iteration of processors
            trace.println("0: "+ storage);
            for(int i = 0; i < n; i++) {
                trace.println((i+1) + ": " + processor[i]);
            }
            trace.println();

            // print to report
            if(n == 1) {
                report.print(n + " processor: ");
            } else {
                report.print(n + " processors: ");
            }
            avgWaitTime = (float)totalWaitTime/m;
            report.print("totalWait=" + totalWaitTime + ", ");
            report.print("maxWait=" + maxWaitTime + ", ");
            report.printf("averageWait=%.2f", + avgWaitTime);
            report.println();

            // reset the storage queue back to its original state
            resetBackUp(m, backUp, storage);

        } // end for

        trace.close();
        report.close();

    } // end main
}