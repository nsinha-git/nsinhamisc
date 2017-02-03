# tictactoe
version 1 on master
Author: Nish
This implements a tic tac toe game(Ancient Egyptian version where a player only uses either a X or O irrespective if he is first player) where computer uses a game tree search to find the best solution.The graphics library used is QT 4.8.



While tic tac toe is a very trivial game to impelement with modern computing power the project is mainly a show case of generic code structure that has more universal ramifications.The aspects of game-tree ,alpha -beta pruning,dynamic programming stand out as some of them. 
The other aspect to observe is modularity of "concepts" in programming. The concepts are captured with lot more ease with OOPS. The expresiveness of C++11 also adds to this. Checkout how shared pointers eases the implementation of dynamic tables giving efficiency of c++ and still keeping the concept clear. The main modules used in this code are:
1.QT interfacing code.
2.Analytics code
2a. Dynamic program to speedup analytics.

Addendum:
Short proof that depth=4 search tree is sufficient for the computer to not loose.

Max holes in tic-tac M=9----------------(1)
Let's say optimal depth is N.

Then N<=M gives a upper bound.i.e N<=9.---------------(2)

Lemma1: If computer controls the mid of board then 4 moves are sufficient.
1.The only way adversary can win is by horizontal patch or vertical patch but not a diagonal patch.
2.Starting from any filled location the adversary needs atleast 2 moves to complete the patch.
So the minimum depth must be twice of the advesary moves i.e N>=2*2=4.----------(3)
Let there exists a patch P consiting of A1,A2,A3 moves that adversary can finish in 3 moves.
Corresponding moves for computer is B1 B2( no B3 as game will be finished).

B2 will not be able to mitigate A3 implies that there exists atleast two options to finish A3.
I claim if computer controls the midpoint then there can only be two alternatives for A3.This 
follows from a hozrizontal and vertical completions. More horizontal completions and vertical 
completions will be  mitigated by B1 if at all.

The claim is that there exists a B1 move that forces the adversary to  drop A2 move leading to A3 move.
At B1 stage computer can see next two moves of adversary and hence knows about A2A3.If a mitigating B1 can
be found then it can be sufficinetly found with depth 4 at stage of B1.Any more depth does not help.
So, N=4.as claimed ---------------------------(4)

/Lemma1
Lemma2:If computer does not control middle and if controls one of corners and still 4 moves suffices.
1.Any B1 has a B2 as a patch going through middle  or no such B2(2) if computer occupies B2 cell.Computer should
 kill this patch and depth of 2 is sufficient.
2. If no patching B2 exist then computer occupies that B2 cell.There exist two more options that patches with B1 (horizontal or vertical)
both at two moves distance.Computer can nullify both the moves in depth=4 search.
so N=4 as claimed.-----------------------(5)

Lemma2
Addendum
