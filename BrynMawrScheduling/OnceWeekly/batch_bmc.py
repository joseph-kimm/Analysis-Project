from subprocess import call

call(["java", "ScheduleMakerOnce", "Constraints/F2000_con.txt", "Prefs/F2000_pref.txt", "Schedules/F2000_schedule.txt"])
for i in range(2001, 2015):
    # For spring course
    constraint = f"Constraints/S{i}_con.txt"
    preference = f"Prefs/S{i}_pref.txt"
    schedule = f"Schedules/S{i}_schedule.txt"
    call(["java", "ScheduleMakerOnce", constraint, preference, schedule])

    # For fall course
    constraint = f"Constraints/F{i}_con.txt"
    preference = f"Prefs/F{i}_pref.txt"
    schedule = f"Schedules/F{i}_schedule.txt"

    call(["java", "ScheduleMakerOnce", constraint, preference, schedule])

call(["java", "ScheduleMakerOnce", "Constraints/S2015_con.txt", "Prefs/S2015_pref.txt", "Schedules/S2015_schedule.txt"])
