#include"tictactoe.hpp"
#include"commoninclude.hpp"
#include <QtGui>
#include<iostream>
using namespace std;
/*main constructor of tictactoe widget.This is kid of mainwindow widget*/
tictactoe::tictactoe(QWidget *parent):QWidget(parent) {
	setFocusPolicy(Qt::StrongFocus);
	color=new QColor ((QRgb) 0xCC66CC); 
	reset();
}

/*resets the board*/
void tictactoe::reset() {
	started=false;
	clearBoard();
	charmap_init(USER);
}
/* draws the board*/
void tictactoe::redrawBoard(QPainter *painter) {
     	QRect rect = contentsRect();
	static int cnt=0;
	int xtopl,ytopl,xbotr,ybotr;
	rect.getCoords(&xtopl,&ytopl,&xbotr,&ybotr);
	//segment the axis in three intervals
	int deltax = (xbotr-xtopl)/3;
	int deltay = (xbotr-xtopl)/3;
	generateXYvals(xtopl,ytopl,deltax,deltay);
	//draw lines to mimic grid 
	drawGridLines(painter);
	drawBoardChars(painter);
	::tictac_config t_config(board);
	analyze.set_config(t_config);
	if(analyze.is_game_done()){
		finish_the_game(painter);
		if(cnt==0) emit putUpBanner("game finished");
		cnt++;
	}else {cnt =0;}
}
/* mouse press location mapping to the correct rect area on board*/
QRect tictactoe::maptorect(int index_x,int index_y) {
	int xl=xval_tuple.at(index_x);	
	int xr=xval_tuple.at(index_x+1);	
	int yl=yval_tuple.at(index_y);	
	int yr=yval_tuple.at(index_y+1);	
	return QRect(xl,yl,xr-xl,yr-yl);
}
/* draw the X and O from board onto the widget*/
void tictactoe::drawBoardChars(QPainter *painter) {
	painter->setPen(color->dark());
	for(int i=0;i<3;i++)
	for(int j=0;j<3;j++) {
		if(board[i][j]) {
			//get the rect from the xval_tuple and yval_tuple
			QRect rect=maptorect(i,j);
			char placeholder[2]={0};
			placeholder[0]=board[i][j];
        		painter->drawText(rect, Qt::AlignCenter, tr(placeholder));
		}
	}
}
/*draw the griddraw the grid size aware*/
void tictactoe::drawGridLines(QPainter *painter) {
	painter->setPen(color->light());
	drawGridLine(painter,xval_tuple.at(0),yval_tuple.at(0),xval_tuple.at(0),yval_tuple.at(3));
	drawGridLine(painter,xval_tuple.at(1),yval_tuple.at(0),xval_tuple.at(1),yval_tuple.at(3));
	drawGridLine(painter,xval_tuple.at(2),yval_tuple.at(0),xval_tuple.at(2),yval_tuple.at(3));
	drawGridLine(painter,xval_tuple.at(3),yval_tuple.at(0),xval_tuple.at(3),yval_tuple.at(3));
	drawGridLine(painter,xval_tuple.at(0),yval_tuple.at(0),xval_tuple.at(3),yval_tuple.at(0));
	drawGridLine(painter,xval_tuple.at(0),yval_tuple.at(1),xval_tuple.at(3),yval_tuple.at(1));
	drawGridLine(painter,xval_tuple.at(0),yval_tuple.at(2),xval_tuple.at(3),yval_tuple.at(2));
	drawGridLine(painter,xval_tuple.at(0),yval_tuple.at(3),xval_tuple.at(3),yval_tuple.at(3));
}
/*finish the game line*/
void tictactoe::drawGridLinethroughMean(QPainter *painter,pair<int,int>l,pair<int,int>r) {
	painter->setPen(color->dark());
	drawGridLine(painter,(xval_tuple.at(l.first)+xval_tuple.at(l.first+1))/2,(yval_tuple.at(l.second)+yval_tuple.at(l.second+1))/2,(xval_tuple.at(r.first)+xval_tuple.at(r.first+1))/2,(yval_tuple.at(r.second)+yval_tuple.at(r.second+1))/2);
}
/*utility to draw lines*/
void tictactoe::drawGridLine(QPainter *painter,int x ,int y,int x1,int y1) {
	painter->drawLine(x,y,x1,y1);
}
/*segment the axes to get 3x3 grid*/
void tictactoe::generateXYvals(int& xtopl,int& ytopl,int& deltax,int& deltay){
	vector<int> x={xtopl,xtopl+deltax,xtopl+2*deltax,xtopl+3*deltax};
	vector<int> y={ytopl,ytopl+deltay,ytopl+2*deltay,ytopl+3*deltay};
	xval_tuple=x;
	yval_tuple=y;
}
/*clears the board*/
void tictactoe::clearBoard() {
	whoseTurn=USER;
	for(int i=0;i<3;i++)
	for(int j=0;j<3;j++)
		board[i][j]=0;
}
/*QT slot for computer playing first*/
void tictactoe::computerplaysfirst() {
	start();
	whoseTurn=COMPUTER;
	charmap_init(COMPUTER);
}
/*table for which player owns which char in tic tac toe*/
void tictactoe::charmap_init(Player x){
	charmap.insert(make_pair(x,'X'));
	Player y=opposite_player(x);
	charmap.insert(make_pair(y,'O'));
}
/*find the opposite of given player e.g COMP->USER and vice-versa*/
Player tictactoe::opposite_player(Player x)const{
	if(x==USER) 
		return COMPUTER;
	return USER;
}

/*slot for start button*/
void tictactoe::start() {
	started=true;
	clearBoard();
	::tictac_config t_config(board);
	analyze.set_config(t_config);
	update();
}
/*draws the last line to finish the game*/
void tictactoe::finish_the_game(QPainter *painter) {
	auto p=analyze.get_finish_points();
	drawGridLinethroughMean(painter,p.first,p.second); 
}
/*entry point when mouse is pressed on board to make a move*/
void tictactoe::mousePressEvent(QMouseEvent *event){
	if(!started)return; 
	if(analyze.is_game_done())return;
	if(whoseTurn == COMPUTER) return;
	int index_x,index_y;
	map_x_y(event->x(),event->y(),&index_x,&index_y);
	if(get_gridpoint(index_x,index_y))
		return;//already written field
	set_gridpoint(index_x,index_y);
	whoseTurn=opposite_player(whoseTurn);
	::tictac_config t_config(board);
	analyze.set_config(t_config);
	update();
}
/*maps to real cordinate in GUI*/
int tictactoe::get_gridpoint(int index_x,int index_y){
	return (int)board[index_x][index_y];
}
/* sets the char on board*/
void tictactoe::set_gridpoint(int index_x,int index_y){
	board[index_x][index_y]=maptochar(whoseTurn);
}
/*player->char*/
char tictactoe::maptochar(Player x)const{
	char c=playertochar(x);
	return c;
}
/*Player to character to fill*/
char tictactoe::playertochar(Player x)const{
	return charmap.find(x)->second;
}
/*maps to correct x y axes based on index*/
void tictactoe::map_x_y(int x,int y,int *index_x,int *index_y) const{
	*index_x=-1;
	*index_y=-1;
	for_each(begin(xval_tuple),end(xval_tuple),[&x,&index_x](int test) {
		if(x>test)*index_x+=1; });
	for_each(begin(yval_tuple),end(yval_tuple),[&y,&index_y](int test) {
		if(y>test)*index_y+=1; });
}
/*QT main draw entry point*/
void tictactoe::paintEvent(QPaintEvent *event) {
     	QPainter painter(this);
	static int cnt;
	if(started && COMPUTER==whoseTurn && !analyze.is_game_done()) {
		::tictac_config t_config(board);
		analyze.set_config(t_config);
		try {
			auto pos=analyze.get_next_move(maptochar(COMPUTER));
			set_gridpoint(pos.first,pos.second);
			whoseTurn=opposite_player(whoseTurn);
			::tictac_config t_config_1(board);
			analyze.set_config(t_config_1);
		} catch(...) {
			//computer cant  move
			//if(cnt==0) {emit putUpBanner("no more moves remaining");}
			cnt++;
		}
	}
	redrawBoard(&painter);
}
