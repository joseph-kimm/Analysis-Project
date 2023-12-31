from subprocess import call

#For fall courses

for i in range(2000, 2015):
    constraint = f"Constraints/F{i}_con.txt"
    preference = f"Prefs/F{i}_pref.txt"
    overlap_schedule = f"Schedules/F{i}_o_schedule.txt"
    non_overlap_schedule = f"Schedules/F{i}_n_schedule.txt"
    zoom_schedule = f"Schedules/F{i}_z_schedule.txt"

    call(["java", "ScheduleMakerZoom", "z", constraint, preference, zoom_schedule])
    #call(["java", "ScheduleMakerTime", "n", constraint, preference, non_overlap_schedule])


#For spring courses
for i in range(2001, 2016):
    constraint = f"Constraints/S{i}_con.txt"
    preference = f"Prefs/S{i}_pref.txt"
    overlap_schedule = f"Schedules/S{i}_o_schedule.txt"
    non_overlap_schedule = f"Schedules/S{i}_n_schedule.txt"
    zoom_schedule = f"Schedules/S{i}_z_schedule.txt"

    call(["java", "ScheduleMakerZoom", "z", constraint, preference, zoom_schedule])
    #call(["java", "ScheduleMakerTime", "n", constraint, preference, non_overlap_schedule])
