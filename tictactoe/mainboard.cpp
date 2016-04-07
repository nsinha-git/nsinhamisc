#include"commoninclude.hpp"
#include"mainboard.hpp"
#include<iostream>

using namespace std;

/*initializes the signals slots buttons as required for QT application*/
mainboard::mainboard()
{
	tic = new tictactoe();
	computerplaysfirst = new QPushButton(tr("Computer Plays First"));
	computerplaysfirst->setFocusPolicy(Qt::NoFocus);
	start = new QPushButton(tr("Start"));
	start->setFocusPolicy(Qt::NoFocus);
	quit = new QPushButton(tr("Quit"));
	quit->setFocusPolicy(Qt::NoFocus);
	connect(computerplaysfirst, SIGNAL(clicked()), tic, SLOT(computerplaysfirst()));
	connect(start, SIGNAL(clicked()), tic, SLOT(start()));
	connect(quit, SIGNAL(clicked()), qApp, SLOT(quit()));
	connect(tic, SIGNAL(putUpBanner(QString)), this, SLOT(banner(QString)));
	
	QGridLayout *layout = new QGridLayout;
	layout->addWidget(tic, 0, 0,6,0);
	layout->addWidget(computerplaysfirst,10,1);
	layout->addWidget(start,10,2);
	layout->addWidget(quit,10,3);
	setLayout(layout);
	setWindowTitle(tr("TicTacToe"));
     	resize(500, 500);
}

/*creates banner when game is finished*/
void mainboard::banner(QString s){
	QMessageBox msgBox;
	msgBox.setText(s);
	msgBox.exec();
}

mainboard::~mainboard(){
	delete tic,delete start,delete quit,delete computerplaysfirst;
}

