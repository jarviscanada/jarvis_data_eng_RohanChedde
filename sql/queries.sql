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

-- Basics
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

-- Join Queries
-- Question 12: Retrieve the start times of members' bookings
SELECT starttime FROM cd.bookings
INNER JOIN cd.members
ON cd.members.memid = cd.bookings.memid
WHERE cd.members.firstname='David'
AND cd.members.surname='Farrell';

-- Question 13: (three joins) Work out the start times of bookings for tennis courts
select bks.starttime as start, facs.name as name
	from 
		cd.facilities facs
		inner join cd.bookings bks
			on facs.facid = bks.facid
	where 
		facs.name in ('Tennis Court 2','Tennis Court 1') and
		bks.starttime >= '2012-09-21' and
		bks.starttime < '2012-09-22'
order by bks.starttime;

-- Question 14: (Three joins) Produce a list of all members, along with their recommender
SELECT mems.firstname, mems.surname, recs.firstname, recs.surname
FROM cd.members mems
LEFT OUTER JOIN cd.members recs
ON recs.memid = mems.recommendedby
ORDER BY mems.surname, mems.firstname;

-- Question 15: (subquery and join) Produce a list of all members, along with their recommender, using no joins
SELECT DISTINCT mems.firstname || ' ' ||  mems.surname AS member,
	(SELECT recs.firstname || ' ' || recs.surname AS recommender 
		FROM cd.members recs 
		WHERE recs.memid = mems.recommendedby
	)
	FROM 
		cd.members mems
ORDER BY member;  

--Aggregation
-- Question 16: (Group by order by) Count the number of recommendations each member makes.
SELECT recommendedby, count(*) 
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby asc;

-- Question 17: (Group by order by) List the total slots booked per facility
SELECT facid, sum(slots) as "Total Slots"
FROM cd.bookings 
WHERE facid IS NOT NULL 
GROUP BY facid
ORDER BY facid;

-- Question 18: (group by with condition) List the total slots booked per facility in a given month
SELECT facid, sum(slots) FROM cd.bookings
WHERE starttime >= '2012-09-01' AND starttime < '2012-10-01'
GROUP BY facid
ORDER BY sum(slots);

-- Question 19: (group by multi col) List the total slots booked per facility per month
SELECT facid, extract(month from starttime) as month, sum(slots) FROM cd.bookings
WHERE extract(year from starttime) = 2012
GROUP BY facid, month
ORDER BY facid, month;

-- Question 20: (count distinct) Find the count of members who have made at least one booking
SELECT COUNT(DISTINCT memid) FROM cd.bookings;

-- Question 21: (group by multiple cols, join) List each member's first booking after September 1st 2012
SELECT mems.surname, mems.firstname, mems.memid, min(bks.starttime) AS starttime
	FROM cd.bookings bks
	INNER JOIN cd.members mems ON
		mems.memid = bks.memid
	WHERE starttime >= '2012-09-01'
	GROUP BY mems.surname, mems.firstname, mems.memid
ORDER BY mems.memid; 

-- Question 22: (Window function) Produce a list of member names, with each row containing the total member count
SELECT COUNT(*) over(), firstname, surname
FROM cd.members
ORDER BY joindate;

-- Question 23: (window function) Produce a numbered list of members
SELECT row_number() over(ORDER BY joindate), firstname, surname
	FROM cd.members
ORDER BY joindate;

-- Question 24: (window function, subquery, group by) Output the facility id that has the highest number of slots booked, again
SELECT facid, total FROM (
	SELECT facid, sum(slots) total, rank() over (ORDER BY sum(slots) DESC) rank
        	FROM cd.bookings
		GROUP BY facid
	) AS ranked
	WHERE rank = 1;

-- String
-- Question 25: (format string) Format the names of members
SELECT surname || ', ' || firstname AS name FROM cd.members;

-- Question 26: (WHERE + string function) Find telephone numbers with parentheses
SELECT memid, telephone FROM cd.members WHERE telephone ~ '[()]';          

-- Question 27:(group by, substr) Count the number of members whose surname starts with each letter of the alphabet
SELECT substr (mems.surname,1,1) AS letter, COUNT(*) AS COUNT 
    FROM cd.members mems
    GROUP BY letter
    ORDER BY letter;
