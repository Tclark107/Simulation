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

    // Test this after you write a Enqueue
    // resets the backUp[] job array and the storage Queue that we manipulate the other Queues with
    static void resetBackUp(int numOfJobs, Job[] arrayOfJobs, Queue storageQ) {
        storageQ.dequeueAll();
        for(int i = 0; i < numOfJobs; i++) {
            arrayOfJobs[i].resetFinishTime();
            storageQ.enqueue(arrayOfJobs[i]);
        }
    }

    // I dont think I need this because if you update a Job Array, it will update the Q
    // this happens after the dQ call on the front
    // it is to make sure that we only compute the first element in a queue
    static void updateProcessor(Queue processor, int start, int end, Job[] proJobs) {
        //trace.println("before dQAll"+processor)
        if(!processor.isEmpty()) {
            processor.dequeueAll();
            //trace.println("after dQAll"+processor)
        }
        for(int i = start;i <= end;i++) processor.enqueue(proJobs[i]);
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
        //trace.print("list of fTimes: ");
        //printArr(finishTimes);
        //trace.println("fTCurr, FTcount, current p depart time:" + start+ ", " +end+ ", "+newItem);
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

    static void processArrival(Queue processor, Job nextJob, int time) {
        if(processor.isEmpty()) {
            nextJob.computeFinishTime(time);
            processor.enqueue(nextJob);
        } else {
            processor.enqueue(nextJob);
        }
    }

    static void printProcessor(Job[][] jobArr, int processor) {
        System.out.print("This processor has the values: ");
        for(int i = 0; i < jobArr[processor].length; i++) {
            System.out.print(jobArr[processor][i]+ ", ");
        }
        System.out.println();
    }

    // moves the jobCursor to the next element 
    static int processFinishTime(Job[][] jobArr, int processor, int currJob, int currentDTime) {
        //trace.println("current time: " + currentDTime);
        //trace.println("processor: " + processor + ", currJob: " + currJob);
        //printProcessor(jobArr, processor);
        jobArr[processor][currJob].computeFinishTime(currentDTime);
        //printProcessor(jobArr, processor);

        //trace.println("the finish time at the current job on the job array: " + jobArr[processor][currJob].getFinish());
        return jobArr[processor][currJob].getFinish();
    }

    // helper function that prints the job Array because I keep having to do it
    static void printJobArray(Job[] jobArray, int numJobs, PrintWriter myout) {
        //trace.println("job array :");
        for(int i = 0; i < numJobs; i++) myout.print(jobArray[i]);
        myout.println();
    }

    static void printQueue(Queue processor) {
        System.out.println("Queue:");
        System.out.println(processor);
    }

    public static void main(String[] args) throws IOException{
        if(args.length!=1){
            Usage();
        }
        
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
        boolean printFlag = true;

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
                    //trace.println("FinishTime at current: " + finishTimes[fTimeCurr] + ", time: " + time);
                    for (int i = 0; i < n; i++) {
                        //trace.println("processor:" + (i+1) + ", departures");
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
                    //trace.println("arrivaltime: " + arrivalTime + " time: " + time);
                    for(int i = 0; i < n; i++) {
                        //trace.println("processor:" + (i+1) + ", arrivals");
                        //trace.println("jobArrayCounter" + jobArrayCounter + " backUpCounter: " + backUpCounter);
                        jobArray[i][jobArrayCounter[i]] = backUp[backUpCounter];
                        //printJobArray(jobArray[i], jobArrayCounter); // print the current job array
                        // may or may not use this
                        //processArrival(processor[i], jobArray[jobArrayCounter], time);
                        //trace.println("mL: " + minLength + ", mLC: " + minLengthCounter);
                        //trace.println("p.l(): " + processor[i].length());
                        //trace.println("p[i]: " + processor[i]);
                        if(processor[i].length() == minLength) {
                            minLengthCounter--;
                            if(processor[i].isEmpty()) {
                                dTime[i] = processFinishTime(jobArray, i, jobCursor[i]-1, arrivalTime);
                                fTimeCounter = iSort(finishTimes, fTimeCurr, fTimeCounter, dTime[i]);
                                processor[i].enqueue(storage.dequeue());
                            } else {
                                processor[i].enqueue(storage.dequeue());
                            }
                            //trace.println("storage: "+storage);
                            //trace.println("pi: "+ processor[i]);
                            //printJobArray(backUp, m);
                            jobsActive++;
                            jobsLeft--;
                            jobArrayCounter[i]++;
                            //trace.println("jAC: " + jobArrayCounter + " jA: " + jobsActive + " jL: "+jobsLeft );

                            // update arrival time and time
                            //trace.println("bAC"+backUpCounter);
                            if(backUpCounter < m-1)temp = backUp[backUpCounter+1].getArrival();
                            //trace.println("bAC"+backUpCounter);
                            //trace.println("temp after update: "+ temp );
                            //trace.println("arrivalTime after update: "+ arrivalTime );
                            if(jobsLeft == 0) temp = 0;
                            backUpCounter++;
                            if(temp != arrivalTime) {
                                arrivalTime = temp;
                                //trace.println("bAC"+backUpCounter);
                                breakFlag = true;
                                printFlag = true;
                            }
                        }

                        // if minLengthCounter is 0, loop through the processors and count the processors at mL
                        //trace.println("mLC after enQ: " + minLengthCounter);
                        //trace.println("mL after enQ: " + minLength + ", mLC after enQ: " + minLengthCounter);
                        if(minLengthCounter == 0) {
                            //trace.println("p.len() if MLC is 0: " + processor[i].length());
                            minLength = processor[i].length();
                            //trace.println("mL after change: " + minLength);
                            for(int j = 0; j < n; j++) {
                                if(processor[j].length() == minLength) minLengthCounter++;
                            }
                        }
                        //trace.println("mL: " +minLength);

                        if(breakFlag) {
                            breakFlag = false;
                            break;
                        }
                    } // end for
                } // end if

                if(printFlag && jobsActive != 0 && jobsLeft != m) {
                    //trace.println("time = " + time);
                    trace.println("0: "+ storage);
                    for(int i = 0; i < n; i++) {
                        trace.println((i+1) + ": " + processor[i]);
                    }
                    trace.println();
                }

                // process next time
                //trace.println("jL:" + jobsLeft);
                //trace.println("fT[curr]: " + finishTimes[fTimeCurr] + " aT: " + arrivalTime);
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

            trace.println("0: "+ storage);
            for(int i = 0; i < n; i++) {
                trace.println((i+1) + ": " + processor[i]);
            }
            trace.println();

            // reset the storage queue back to its original state
            resetBackUp(m, backUp, storage);

        } // end for

        trace.close();
        report.close();

    } // end main
}