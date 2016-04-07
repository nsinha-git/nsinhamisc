#ifndef analyze_game_hpp
#define analyze_game_hpp
#include<stdio.h>
#include<iostream>
#include<exception>
#include<vector>
#include<map>
#include<list>
#include<utility>
#include<functional>
#include<algorithm>
#include"dynamic_programming.hpp"
using namespace std;
using namespace std::placeholders;
/*the configuration of analysis engine*/

class tictac_config{
public:
#define F_I_J_3 for (int i=0;i<3;i++) for (int j=0;j<3;j++)
	tictac_config(){
		F_I_J_3 board[i][j]=0;
	}
	tictac_config(char x[3][3]){
		F_I_J_3 board[i][j]=x[i][j];
	}
	tictac_config(tictac_config *x){
		F_I_J_3 board[i][j]=x->board[i][j];
	}
	tictac_config(tictac_config &&x){
		F_I_J_3 board[i][j]=x.board[i][j];
	}
	tictac_config(const tictac_config &x){
		F_I_J_3 board[i][j]=x.board[i][j];
	}
	tictac_config & operator =(tictac_config &x){
		F_I_J_3 board[i][j]=x.board[i][j];
		return *this;
	}
	bool operator<(const tictac_config &y){
		F_I_J_3 {
			if(board[i][j]>y.board[i][j])return true;
			else  if(board[i][j]<y.board[i][j])return false;
		}
		return false;
	}
		
	void set(pair<int,int>p,char x){board[p.first][p.second]=x;}
	char board[3][3];
};

class tictac_analyze_game:public dynamic_program<tuple<tictac_config,char,int>,tuple<int,int,int> >
{
	using dyn_par=dynamic_program<tuple<tictac_config,char,int>,tuple<int,int,int> >;
public:
	tictac_analyze_game();
	/*cons from existing analysis.used in evaluation of moves*/
	tictac_analyze_game(tictac_analyze_game &a):dyn_par(a){set_config(a.config);}
	tictac_analyze_game(tictac_analyze_game *a,tictac_config &c):dyn_par(*a){set_config(c);}
	void set_config(tictac_config&) ;//configures the state
	bool is_game_done()const;//returns if game is over
	pair<pair<int,int>,pair<int,int>> get_finish_points() noexcept(false);//return the points that finish the game.

	pair<int,int> get_next_move(char) throw(runtime_error);//compute best next move
private:
	tictac_config config;//configuration
	bool check_consecutive_chars() const;//used in finding if the game is finished by checking if 3 conse pos have same value
	bool check_horizontal_chars() const;
	bool check_vertical_chars() const;
	bool check_diagonal_chars() const;
	mutable bool finished=0;//set when game is over
	mutable pair<pair<int,int>,pair<int,int>> finish_points;//finishing points
	tuple<int,int,int> eval_all_moves(char side,int depth) throw(runtime_error);//evaluate all moves for computer
	void eval_this_move(pair<int,int>pos,::tictac_config in_config,char side,int depth,vector<tuple<int,int,int>>& in_utility_tuple);
};


#endif
