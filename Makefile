demo:
	rm demonst_*
	javac ScheduleMaker.java
	perl make_random_input.pl 400 2000 10 50000 demonst_con.txt demonst_pref.txt
	java ScheduleMaker demonst_con.txt demonst_pref.txt demonst_schedule.txt
	perl is_valid.pl demonst_con.txt demonst_pref.txt demonst_schedule.txt

demo_one:
	java ScheduleMaker demonstration/demonst_con_1.txt demonstration/demonst_pref_1.txt schedule.txt
	perl is_valid.pl demonstration/demonst_con_1.txt demonstration/demonst_pref_1.txt schedule.txt

demo_two:
	java ScheduleMaker demonstration/demonst_con_2.txt demonstration/demonst_pref_2.txt schedule.txt
	perl is_valid.pl demonstration/demonst_con_2.txt demonstration/demonst_pref_2.txt schedule.txt

demo_three:
	java ScheduleMaker demonstration/demonst_con_3.txt demonstration/demonst_pref_3.txt schedule.txt
	perl is_valid.pl demonstration/demonst_con_3.txt demonstration/demonst_pref_3.txt schedule.txt


	