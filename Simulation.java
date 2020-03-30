//-----------------------------------------------------------------------------------------------------------
// Simulataion.java
// This is my second attempt at writing this algorithm.
// The first one was a mess
// Tristan Clark
//-----------------------------------------------------------------------------------------------------------

import java.io.*;
import java.util.Scanner;

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
    static void resetBackUp(int numOfJobs, Job[] arrayOfJobs, Queue storageQ) {
        for(int i = 0; i < numOfJobs; i++) {
            arrayOfJobs[i].resetFinishTime();
            storageQ.enqueue(arrayOfJobs[i]);
        }
    }

    public static void main(String[] args) throws IOException{
        if(args.length!=1){
            Usage();
        }
        
        Scanner in = new Scanner(new File(args[0]));
        // number of jobs
        int m = Integer.parseInt(in.nextLine());
        System.out.println("m: " + m);

        // initialize a backup array with the jobs
        Job[] backUp = new Job[m];
        for(int i = 0; i < m; i++) backUp[i] = getJob(in);
        //for(int i = 0; i < m; i++) System.out.print(backUp[i]); 
        //System.out.println();

        // initialize the storage Queue that we will use to keep track of jobs left
        int jobsLeft;
        Queue storage = new Queue();
        //System.out.println(storage);

        // keeps track of how many processors are active
        int processorsActive;

        // run the program on n processor Queues
        for(int n = 1; n < m; n++) {

            // test this
            // reset the storage queue back to its original state
            //resetBackUp(m, backUp, storage);
            jobsLeft = m;
            processorsActive = 0;

            // while there are jobsLeft and processors still processing jobs, continue
            while(jobsLeft != 0 || processorsActive != 0){

                // process time
                // time is equal to the smaller of the next Arrival and the next Finish
                // set temp to min

                // loop through the processor queues do all jobs at time
                // if departuretime == time
                    //for (i to n-1)
                        // process departure()
                        // change departure time to the next dTime
                        // if time changed then break
                // if arrivaltime == time
                    //for(i to n-1)
                        // process arrival()
                        // change arrivaltime to next aTime on backup
                        // if the time changed then break

            } // end while

        } // end ford

    } // end main
}