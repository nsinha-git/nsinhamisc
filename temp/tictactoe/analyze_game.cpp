#include "analyze_game.hpp"

#define F_I_J_3 for (int i=0;i<3;i++) for (int j=0;j<3;j++)
/*this implements the alanysis engine doing 2 jobs:
1.Find if the game has finshed
2.Finding best move for computer
*/
static bool pred_fn_for_eval_all_moves(tuple<int,int,int> x,tuple<int,int,int> y); 
static char nextside(char side){if(side=='X')return 'O';return 'X';}

bool compare(tuple<tictac_config,char,int>x,tuple<tictac_config,char,int>y)
{
        if(get<2>(x)<get<2>(y)) return true;
        else  if(get<2>(y)<get<2>(x)) return false;
        if(get<1>(x)<get<1>(y)) return true;
        else  if(get<1>(y)<get<1>(x)) return false;
        return get<0>(x)<get<0>(y);
}

tictac_analyze_game::tictac_analyze_game():dyn_par(compare) {
}

void tictac_analyze_game::set_config(tictac_config& c)  {
	config=c;	
	finished=false;
}

bool tictac_analyze_game::is_game_done() const {
	if(finished) return true;
	return finished = check_consecutive_chars();
}

bool tictac_analyze_game::check_consecutive_chars() const {
	/*check all the 8 lines one by one
	---  ||| - -
	---  |||  -
	---  ||| - -
	*/
	bool x= check_horizontal_chars();
	bool y= check_vertical_chars();
	bool z= check_diagonal_chars();
	return x||y||z;
}

bool tictac_analyze_game::check_horizontal_chars() const{
	auto board = config.board;
	if(board[0][0] && (board[0][0]==board[1][0]) &&(board[0][0]==board[2][0]) )
		{
			finish_points=make_pair(make_pair(0,0),make_pair(2,0));
			return true;
		}

	if(board[0][1] && (board[0][1]==board[1][1]) &&(board[0][1]==board[2][1]) )
		{
			finish_points=make_pair(make_pair(0,1),make_pair(2,1));
			return true;
		}
	if(board[0][2] && (board[0][2]==board[1][2]) &&(board[0][2]==board[2][2]) )
		{
			finish_points=make_pair(make_pair(0,2),make_pair(2,2));
			return true;
		}
		return false;
}
bool tictac_analyze_game::check_vertical_chars() const {
	auto board = config.board;
	if(board[0][0] && (board[0][0]==board[0][1]) &&(board[0][0]==board[0][2]) )
		{
			finish_points=make_pair(make_pair(0,0),make_pair(0,2));
			return true;
		}

	if(board[1][0] && (board[1][0]==board[1][1]) &&(board[1][0]==board[1][2]) )
		{
			finish_points=make_pair(make_pair(0,1),make_pair(2,1));
			return true;
		}
	if(board[2][0] && (board[2][0]==board[2][1]) &&(board[2][0]==board[2][2]) )
		{
			finish_points=make_pair(make_pair(2,0),make_pair(2,2));
			return true;
		}
		return false;
}
bool tictac_analyze_game::check_diagonal_chars() const {
	auto board = config.board;
	if(board[0][0] && (board[0][0]==board[1][1]) &&(board[0][0]==board[2][2]) )
		{
			finish_points=make_pair(make_pair(0,0),make_pair(2,2));
			return true;
		}
	if(board[0][2] && (board[0][2]==board[1][1]) &&(board[0][2]==board[2][0]) )
		{
			finish_points=make_pair(make_pair(0,2),make_pair(2,0));
			return true;
		}
	return false;
}

pair<pair<int,int>,pair<int,int>> tictac_analyze_game::get_finish_points() noexcept(false){
	if(!is_game_done()) throw(runtime_error("game not done"));
	return finish_points;
}

pair<int,int> tictac_analyze_game::get_next_move(char side) throw(runtime_error) {
	//we have the board and we need to find the best move for computer
	//output is<x,y> which is where a computer should fill the mark
	int depth =4;
	try {
		auto utility_tuple=eval_all_moves(side,depth);
		return make_pair(get<0>(utility_tuple),get<1>(utility_tuple));	
	} catch(...) { throw;}
}	
/*returns positions where next moves can be made*/
list<pair<int,int>> get_moves(char board[3][3]) {
	list<pair<int,int>> l;
	F_I_J_3 {
		if(board[i][j]==0) l.push_back(make_pair(i,j));
	}
	return l;
}
/*returns best move possible depending on current state of game.May throw if no move can be made*/
tuple<int,int,int> tictac_analyze_game::eval_all_moves(char side,int depth) throw(runtime_error)
{
	::tictac_config local_config(config);
	list<pair<int,int>> l_ofmoves=get_moves(local_config.board);
	tuple<int,int,int> lookup_val;
	vector<tuple<int,int,int>>utility_tuple;
	
	if(dyn_par::retrieve(make_tuple(config,side,depth),lookup_val)){
		return lookup_val;
	}
	for(auto i=begin(l_ofmoves);i!=end(l_ofmoves);i++) {
		eval_this_move(*i,local_config,side,depth,utility_tuple);
	}
	sort(begin(utility_tuple),end(utility_tuple),pred_fn_for_eval_all_moves);
	if(begin(utility_tuple)==end(utility_tuple)) throw runtime_error("");
	dyn_par::store(make_tuple(config,side,depth),*begin(utility_tuple));
	return *begin(utility_tuple);
}

bool pred_fn_for_eval_all_moves(tuple<int,int,int> x,tuple<int,int,int> y) {
	/*utility higher*/
	if(get<2>(x)>get<2>(y)) return true;
	if(get<2>(x)<get<2>(y)) return false;
	/*mid of board*/
	if(get<0>(x)==get<1>(x) && get<0>(x)==1) return true;
	if(get<0>(y)==get<1>(y) && get<0>(y)==1) return false;
	/*diagonal elements*/
	if((get<0>(x)+get<1>(x))%2==0) return true;
	if((get<0>(y)+get<1>(y))%2==0) return false;
	return true;
}

static const int MAX_UTILITY = 10;
static const int MIN_UTILITY = 0;
static const int UNDECIDED_UTILITY = 5;

/*each move =pos is evaluated till depth exhausted*/
void tictac_analyze_game::eval_this_move(pair<int,int>pos,::tictac_config in_config,char side,int depth,vector<tuple<int,int,int>>& in_utility_tuple) {
	::tictac_config local_config;
	local_config=in_config;
	local_config.set(pos,side);//move is made
	tictac_analyze_game local_analysis(this,local_config);
	bool is_win=local_analysis.is_game_done();
	if(is_win) in_utility_tuple.push_back(make_tuple(pos.first,pos.second,MAX_UTILITY));
	if((depth-1)==0){
		in_utility_tuple.push_back(make_tuple(pos.first,pos.second,UNDECIDED_UTILITY)); 
		return;
	}
	
	//game is not complete and depth is still positive. go for adversay best shot
	//the utility=10-adversary utility  
	try {
		auto adv_tuple=local_analysis.eval_all_moves(nextside(side),depth-1);
		in_utility_tuple.push_back(make_tuple(pos.first,pos.second,MAX_UTILITY-get<2>(adv_tuple)));
	} catch(...){};
		in_utility_tuple.push_back(make_tuple(pos.first,pos.second,MIN_UTILITY));
}
