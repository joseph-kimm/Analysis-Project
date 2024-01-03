# Analysis-Project
Group project for CMSC B340, Analysis of Algorithms.

Contributors: Renata Del Vecchio, Joseph Kim, Bridge Schaad

# Description
This project is designed to support a university's registrar office by creating a class schedule that enables students to enroll in their preferred classes. During the registration process, the registrar must gather a list of 4 preferred classes from every student, then use this data to support students in enrolling in as many of their preferred classes as possible. They must also avoid any "conflicts": student  enrolled in multiple classes at the same time, teachers instructing multiple classes at the same time, double-booked classrooms, or classes that have more students than the room can fit.

This project is implemented in Java, and relies on perl scripts to generate randomized input data.

# How to use
If you need to generate input files, first run: 
    `perl <number of room> <number of classes> number of class times> <number of students> <contraint file> <student prefs file>`

Use `run.sh <constraint file> <student prefs file> <schedule>` to quickly generate a schedule text file, provided you already have a constraints and student preferences file.

# Bryn Mawr Scheduling
As an extension of the previously described schedule making algorithm, we considered how our program would performed when applied to a real university's enrollment data. Using historical data from Bryn Mawr College, each folder within **BrynMawrScheduling** supports an  additional constraint:
1. OnceWeekly - Introduces the potential for overlap between time slots. Classes take place exactly once a week.
2. OverlappingTimes 
    - ScheduleMakerTime extends OnceWeekly so that classes take place multiple times a week.
    - ScheduleMakerZoom extends ScheduleMakerTime to introduce online classes, that is, dropping the room capacity constraint for the most popular classes. (This assumes that room capacities are the only constraint to class size and does not account for, say,  teachers' capacity for large classes.)
3. SpecificRooms - Each class must be assigned to a viable room, i.e. a biology lab is guaranteed a lab space, rather than a lecture hall. Viable rooms are specified in the constraints file.
4. Sections - Highly popular classes can be split into multiple sections.


To generate a schedule that accounts for one of the additional cases, within the corresponding folder use `ScheduleMaker[Once/Time/Zoom/Rooms/Sections].java <constraint file> <student prefs file> <schedule>`.