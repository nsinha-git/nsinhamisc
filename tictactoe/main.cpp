#include<iostream>
#include<QApplication>
#include<QLabel>
#include<QWidget>
#include<QPixmap>
#include<QLayout>
#include<QPainter>
#include"mainboard.hpp"

/*calls into QT application that controls the progression of the game*/
int main(int argc,char **argv) {
	QApplication app(argc,argv);
	mainboard w ;
	w.show();
	return app.exec();
}
