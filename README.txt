In this file you should include:

Any information you think we should know about your submission
* Is there anything that doesn't work? Why?
* Is there anything that you did that you feel might be unclear? Explain it here.

A description of the creative portion of the assignment
* Describe your feature
* Why did you choose this feature?
* How did you implement it?


Edward Bottom and Reid Meyer
Project 3 for CSE 438, Mobile Application Development
BlackJack App


On testing, the app works as intended. One small thing that might not be clear is that if the user changes the bet amount to an amount outside of the
allowed range, the bet will default to 0. One other small thing; we made it so there is a pause in between rounds. Each time you have to click start game. 
We decided this because of our creative portion. We talked to Professor Shook about this and he believed this would be the best implementation.


CREATIVE:

Made it so the user can bet. When someone creates an account, the user starts off with 1000 chips. The default value is 0 for bet amount in case they
want to practice. The UI displays the amount of chips, which is a field in the firebase database. The user can change the bet amount and then press 
start game. If the user changes the bet amount during a round, the new bet amount will take effect in the next round. Also, on the leaderboard, we 
made it so there is an option to sort by total wins or total chips earned.

We chose this feature because it is intuitive in blackjack. Poker players want to be able to bet.

We implemented it with an extra field in the firebase database. The user can input the bet into the field, and it is sanitized and checked so 
it doesn't break the system and is updated between games. Intially the player is not forced to bet every time and has the option of not betting as well.

The leader board as required displays wins when it is selected. However, we also give teh user the option to sort the leader board by chips as well as
it seemed most logical to let them see both wins and chips if we were going to track both in the database. 

(10 points) Player can login and login data is stored in Firebase
(10 points) Player’s win/loss counts are displayed on startup
(10 points) The Player receives two cards face up, and the dealer receives one card face up and one card face down
(5 points) Swiping right allows the Player to hit
(5 points) Double tapping allows the Player to stand
(10 points) Cards being dealt are animated, and all cards are visible.
(5 points) If the Player goes over 21, they automatically bust
(10 points) The dealer behaves appropriately based on the rules described above
(10 points) Once the game is complete, the winner should be declared and the Firebase database should be updated appropriately
(5 points) A new round is automatically started after each round
(5 points) Player can logout
(10 points) A leaderboard is shown in a separate tab or activity and is consistent among installations of the app
(15 points) Creative portion!

Everything works exactly as described. You both have submitted quality work all semester and this assignment is no different. Great job!

Total: 110 / 110

