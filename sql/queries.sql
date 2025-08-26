-- Modifying Data
-- Question 1: Insert some data into a table
INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    values (9, 'Spa', 20, 30, 100000, 800);

-- Question 2: Insert calculated data into a table
INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    select (select max(facid) from cd.facilities)+1, 'Spa', 20, 30, 100000, 800;

-- Question 3: Update some existing data
UPDATE cd.facilities SET initialoutlay = 10000 WHERE facid = 1;

-- Question 4: Update a row based on the contents of another row
UPDATE cd.facilities facs
SET membercost = (SELECT membercost * 1.1 FROM cd.facilities WHERE facid = 0),
        guestcost = (SELECT guestcost * 1.1 FROM cd.facilities WHERE facid = 0)
WHERE facs.facid = 1;

-- Question 5: Delete all bookings
DELETE FROM cd.bookings;

-- Question 6: Delete a member from the cd.members table
DELETE FROM cd.members
WHERE memid = 37;

-- Question 7: Control which rows are retrieved
SELECT facid, name, membercost, monthlymaintenance 
    FROM cd.facilities 
    WHERE (membercost < (monthlymaintenance * 0.02)) 
    AND (membercost != 0);

-- Question 8: Basic String Services
SELECT * FROM cd.facilities WHERE name LIKE '%Tennis%';

-- Question 9: Matching against multiple possible values
SELECT * FROM cd.facilities WHERE facid IN (1,5);

-- Question 10: Working with dates
SELECT memid, surname, firstname, joindate FROM cd.members WHERE joindate > '2012-09-01';

-- Question 11: Combining results from multiple queries
SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;

-- Question 12: Retrieve the start times of members' bookings
SELECT bks.starttime 
    FROM cd.bookings bks 
        INNER JOIN cd.members mems
            ON mems.memid = bks.memid
    WHERE
        mems.firstname='David'
        AND mems.surname='Farrell';
