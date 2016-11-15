
CC=ls
RM=rm -rf  *.pdf *.dvi *.out *.log *.aux *.bcf *.xml *.snm *.nav
RML=rm -rf  *.dvi *.out *.log *.aux *.bcf *.xml *.snm  *.nav
PDF=dvipdf

all: create

clean:rm

create:killopenpdf
	-@latex design.tex
	@$(PDF) *.dvi
	@open *.pdf
	@$(RML)

onlycreate:
	-@latex design.tex
	@$(PDF) *.dvi
	@open *.pdf
	$(RML)

createtest:killopenpdf
	-@latex test.tex
	@$(PDF) *.dvi
	@open *.pdf
	@$(RML)

onlycreatetest:
	-@latex test.tex
	@$(PDF) *.dvi
	@open *.pdf
	@$(RML)

rm:killopenpdf
	@$(RM)   
	#@read -p "Press enter to continue"
	
killopenpdf:
	@export A=`ps -ef|grep "AdobeReader"|grep -v "AdobeCrashReporter"|grep "\-psn"|awk '{print $$2}'`;kill -9 $$A
