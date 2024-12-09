create database battleship

use battleship

create table players(
	username nvarchar(50) primary key,
	lastSeen datetime,
	matchesWon int
)

select * from players;

-- alter table players add matchesWon int;

--EXEC sp_rename 'players.loginTime', 'lastSeen', 'COLUMN';

--drop table matches
--drop table players
--drop table [queue]

create table matches(
	mId int identity(1, 1) primary key,
	p1 nvarchar(50),
	p2 nvarchar(50),
	startTime datetime,
	currentPlayer nvarchar(50),
	p1Grid varchar(100),
	p2Grid varchar(100),
	isActive bit,
	winner nvarchar(50),
)

select * from matches;

delete from matches where is_active = 1;

create table queue(
	qId int identity(1, 1) primary key,
	player nvarchar(50) not null,
	grid varchar(100) not null
)

select * from queue;

-- for leaderboard
SELECT 
    winner AS Player,
    COUNT(*) AS Wins
FROM 
    Matches
GROUP BY 
    winner
ORDER BY 
    Wins DESC, 
    Player ASC; -- Secondary sort by name alphabetically in case of ties