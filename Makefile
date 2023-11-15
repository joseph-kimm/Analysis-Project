demonstration:
	javac ScheduleMaker.java
	perl make_random_input.pl 10 40 5 1000 demonst_con.txt demonst_pref.txt
	java ScheduleMaker demonst_con.txt demonst_pref.txt
	perl is_valid.pl demonst_con.txt demonst_pref.txt schedule.txt