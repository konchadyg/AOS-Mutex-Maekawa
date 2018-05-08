# AOS-Mutex-Maekawa

//|     Member 1: Piyush Makani [PXM140430]              |
//|     Member 2: Konchady Gaurav Shenoy [KXS168430]     |
//|     Instructor: Dr. Neeraj Mittal                    |
//|     Oct 2016                                         |
//|     The University of Texas at Dallas                |
//========================================================

Instructions:

1. Find Project2_<netid1_netid2>.zip
2. In the server, place all the files in $HOME/proj2. Create if not existing. 
   You may change the config file if needed.
3. Update the netid variable, present in the launcher and cleanup script.
4. Run the following commands.

	$ cd $HOME/proj2/code
	$ cd javac -verbose *java 
	$ cd ..
	$ chmod -v 755 launcher.sh
	$./launcher.sh
	
5. To check for overlap. 
	$ java CheckOverlap 
	
(Output file output.txt) is generated in $HOME directory

5. To run the cleanup script.
	> open another teminal window and login into a different DCXX host (which is not mentioned in config.txt).
	> 	$ bash
		$ cd $HOME/proj2
		$ chmod -v 755 cleanup.sh
		$ ./cleanup.sh
