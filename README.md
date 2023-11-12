# Analysis-Project
Group project for CMSC B340, Analysis of Algorithms.

Contributors: Renata Del Vecchio, Joseph Kim, Bridge Schaad

# Description
This project is designed to support a university's registrar office to create a class schedule that enables students to enroll in their preferred classes. During the registration process, the registrar must gather a list of 4 preferred classes from every student, then use this data to support students in enrolling in as many of their preferred classes as possible. They must also avoid any student being enrolled in multiple classes at the same time, teachers instructing multiple classes at the same time, classrooms being double-booked, and classes that have more students than the room can fit.

This project is implemented in Java, and relies on perl scripts to generate randomized input data.

# How to use
If you need to generate input files, first run: 
    perl <number of rooms> <number of classes> <number of class times> <number of students> <contraint file> <student prefs file>

Use run.sh to quickly generate a schedule file, provided you already have a constraints and student preferences file.

# Bryn Mawr Scheduling
As an extension of the previously described schedule making algorithm, we considered how our program would performed when applied to a real university's enrollment data. Using historical data from Bryn Mawr College, we updated our main ScheduleMaker file to support additional constraints, including classes with overlapping timeslots.
