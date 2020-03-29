
                            //System.out.println("nft after min" + nextFinishTime);
                        }
                        //System.out.println("nFT: " + nextFinishTime);
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