#include"commoninclude.hpp"
#include<stdio.h>
#include"analyze_game.hpp"
#include<map>
using namespace std;
/*implements the tictac widget QT
It depends on tictac_analysis for main brain behind detmining the state of game
and making computer moves*/


enum Player{USER=0,COMPUTER};
class tictactoe:public QWidget {
	Q_OBJECT
public:
	tictactoe(QWidget *par=NULL);
public://QT fns
	QSize sizeHint() const {
		return QSize(500,500);
	}
	QSize minimumSizeHint() const {
		return sizeHint();
	}
	void reset();
public slots:
	void computerplaysfirst(); 
	void start();
signals:
	void putUpBanner(QString s);
protected:
	void paintEvent(QPaintEvent *event);
	void mousePressEvent(QMouseEvent *event);
private:
	Player opposite_player(Player x) const;
	void map_x_y(int x,int y,int *index_x,int *index_y)const;
	void finish_the_game(QPainter *);
//drawing fns
private ://QTDRAWING:
	void redrawBoard(QPainter *painter);
	void drawBoardChars(QPainter *painter) ;
	void drawGridLines(QPainter *painter);
	void drawGridLinethroughMean(QPainter *painter,pair<int,int>,pair<int,int>);
	void drawGridLine(QPainter *painter,int,int,int,int);
	void generateXYvals (int&,int&,int&,int&);
	void set_gridpoint(int index_x,int index_y);
	int get_gridpoint(int index_x,int index_y);
	QRect maptorect(int index_x,int index_y);
	vector<int> xval_tuple;
	vector<int> yval_tuple;
	QColor *color  ;
//board state 
private ://BOARD:
	void clearBoard();
	void charmap_init(Player );
	char maptochar(Player x) const;
	char playertochar(Player x)const;
	char board[3][3]; 
	bool started;
	Player whoseTurn;
	map<Player,char> charmap;
private ://ANALYZE:
	tictac_analyze_game analyze;
};


