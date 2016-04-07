#include<commoninclude.hpp>
#include"tictactoe.hpp"
/*the class implementing the main widget in QT.*/

class mainboard:public QWidget {
	Q_OBJECT
public:
	mainboard();
	~mainboard();
public slots:
	void banner(QString );
private:
	QLabel *createlabel(const QString &text);
	tictactoe *tic;
	QPushButton *computerplaysfirst;
	QPushButton *start;
	QPushButton *quit;
};
