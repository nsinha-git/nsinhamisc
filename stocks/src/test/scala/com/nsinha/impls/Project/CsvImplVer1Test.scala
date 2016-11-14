package com.nsinha.impls.Project

import java.io.{File, FileInputStream, FileWriter}

import com.nsinha.impls.Project.TimeSeries._
import com.nsinha.data.Csv.Price
import com.nsinha.impls.Project.JsonCsvProject.JsonCsvProjectImpl
import com.nsinha.impls.Project.OrderAnalysis.OrderAnalysis
import com.nsinha.impls.Project.OrderAnalysis.OrderAnalysis.apply
import com.nsinha.impls.Project.QuandlOHLCDump.TimeSeriesRecordsFromYearly._
import com.nsinha.impls.Project.Quotes.Scottrade.{ConsumeAllScottradeQuotes, CsvDailyQuotesScottradeProjectImpl}
import com.nsinha.impls.Project.YearlyQuoteAnalysisProject.YearlyQuoteAnalysisProjectImpl
import com.nsinha.utils.{ConcatenateJsonFiles, FileUtils, Loggable, StringUtils}
import main.scala.com.nsinha.data.Csv.generated.GenCsvQuoteRowScottrade
import org.scalatest.{FunSuite, ShouldMatchers}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration
import scala.concurrent.duration.Duration
import scala.concurrent.duration.{FiniteDuration, SECONDS}
import scaldi.Injectable
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.writePretty

import scala.concurrent.Await
import scala.io.Source

class CsvImplVer1Test extends FunSuite with  ShouldMatchers with Injectable with Loggable {

  val tickers = "ZTS ZOES ZNGA ZLTQ ZION ZFSVF ZEUS ZEN ZBRA ZBH ZAYO YY YUM YPF YORW YNDX YIN YHOO YELP Y XYL XXIA XTLY XRX XRS XRAY XPO XON XLNX XL XEL XEC X WYNN WYN WY WWW WWD WWAV WUBA WU WTS WTR WTM WSTC WST WSO WSFS WSBF WSBC WRK WRI WRE WRB WR WPZ WPPGY WPC WOOF WMT WMS WMMVY WMB WM WLTW WLL WLK WLH WING WHR WGP WGL WFT WFM WFC WEX WEN WEC WDC WDAY WD WCN WCG WCC WBS WBC WBA WB WAT WASH WAL WAGE WAFD WABC WAB W VWR VVI VVC VTR VSTO VSI VSH VSAT VRX VRTX VRSN VRSK VRNT VR VOYA VOD VNTV VNO VMW VMI VMC VLVLY VLP VLO VLKAY VIV VISI VIRT VIPS VIP VIAB VGZ VGR VG VFC VER VEOEY VEEV VECO VC VASC VAR VALE VAL VAC VA V UUGRY UTX UTHR USNA USG USFD USB URBN UPS UNVR UNP UNM UNH UMPQ UMBF ULTI ULTA UIS UHS UHAL UGP UGI UFS UFPI UFI UFCS UE UCFC UBSI UBSH UBNT UAM UAM UAL UA TYL TYEKF TXT TXRH TXN TX TWX TWTR TWOU TWO TWLO TUP TU TTWO TTS TTMI TTM TTDKY TTC TSU TSS TSRO TSO TSN TSM TSLA TSL TSE TSCO TS TRV TRUP TRUE TRU TRST TRQ TRP TRN TRMB TRIP TRI TRGP TREX TRCB TPX TPH TOWR TOWN TOT TOL TOELY TNP TMUS TMP TMO TMK TMHC TMH TM TLYS TLSYY TLN TLLP TLGT TKR TKPPY TJX TIVO TIS TIME TILE TIF THS THRM THO THG THC TGNA TGI TGD TFX TFSL TEX TEVA TER TEP TEO TEN TELNY TEL TEGP TECH TEAM TDY TDS TDG TDC TD TCTZF TCP TCO TCK TCBI TCB TAST TASR TARO TAP TAHO TA T SYY SYT SYRG SYNT SYNA SYMC SYKE SYK SYF SYBT SXT SXL SXI SXC SWX SWN SWKS SWK SWHC SWFT SVU SVMLF SUPV SUP SUN SUM SUI SUHJY SU STX STWD STT STS STRZA STOR STON STN STMP STM STLD STJ STFC STE STBA ST SSRI SSNLF SSNC SSL SSB SRI SRCL SRCE SRC SQ SPWR SPSC SPR SPLS SPLK SPKKY SPKE SPGI SPG SPAR SONS SONC SONA SON SOHU SODA SO SNY SNV SNPS SNPMF SNOW SNN SNI SNH SNCR SNBC SNA SMTC SMP SMI SMG SMFG SMEBF SMCI SLW SLM SLGN SLG SLF SLCA SLB SLAB SKYAY SKX SKT SJW SJR SJM SJI SIX SIVB SITE SIRI SIR SINA SIMO SIGI SIG SID SHW SHOP SHO SHEN SHBI SGMS SGEN SFUN SFS SFR SFNC SFM SFBS SF SEIC SEE SE SDRL SCTY SCSS SCS SCNB SCL SCHW SCHL SCG SCCO SCAI SC SBUX SBSI SBS SBNY SBH SBGL SBBX SBAC SAVE SATS SAP SANM SAND SAIC SAH SAFM SABR SA RYN RYI RYAAY RY RWT RWEOY RUSHA RTN RSPP RS RRGB RRD RRC RPM ROST ROP ROLL ROL ROK ROIC ROCK RNG RMR RMP RMD RMBS RLJ RLI RL RJF RIO RIG RICE RIC RHT RHP RHI RHHBY RGR RGLD RGC RGA RF REXR RES REGN RE RDY RDWR RDS/A RDN RDCM RCL RCII RCI RBA RAX RAI RAD RACE R QVCA QUNR QTWO QTS QSR QRVO QGEN QEP QCRH QCOM Q PZZA PYPL PXD PX PWR PVTB PVH PVG PTR PTLA PTEN PSXP PSX PSTG PSEC PSB PSA PRXL PRU PRTY PRTA PRMW PRLB PRK PRI PRGO PRA PPS PPL PPG PPC PPBI POT POST POR POOL POL PNW PNRA PNR PNM PNFP PNC PM PLXS PLUS PLT PLOW PLNT PLKI PLD PLCE PLAY PKX PKI PKG PINC PII PHM PHD PH PGRE PGND PG PFPT PFGC PFG PFE PFBC PF PEN PEGA PEG PEB PE PDM PDCO PCTY PCRX PCRFY PCMI PCLN PCG PCAR PBR PBH PBCT PBA PB PAYX PAYC PAY PAGP PACW PAC PAAS PAA P OZRK OXY OTTR OTEX OSTK OSK ORLY ORI ORCL ORBK ORBC ORA OPK OPB OMN OMI OMF OME OMCL OLN OLLI OLED OKE OI OHI OGE OFG OEC ODFL OCPNY OCLR OCANF OC OA O NYCB NXTM NXPI NWSA NWS NWN NWE NVZMF NVS NVRO NVR NVO NVDA NVAX NUVA NUE NTT NTRS NTIOF NTGR NTES NTDOY NTCT NTAP NSP NSM NSIT NSC NSANY NRZ NRG NP NOW NOV NORD NOMD NOK NOC NNN NLSN NLS NKTR NKE NJR NINOY NILSY NICE NI NHI NGVT NGL NGHC NGG NGD NFX NFLX NFG NFBK NEWR NEU NEOG NEO NEM NEE NE NDSN NDAQ NCS NCR NCLH NBTB NBR NBL NBIX NAVG NAV NATI NANO N MYL MYCC MXL MXIM MWA MUSA MU MTZ MTX MTSI MTN MTLHY MTG MTDR MTD MTCH MTB MT MSTR MSI MSGN MSG MSFT MSCI MSCC MSBI MSADY MSA MS MRVL MRK MPWR MPW MPLX MPEL MPC MPAA MOV MOS MORN MON MOG/A MOD MO MNST MNRO MNR MNK MNI MMYT MMS MMM MMC MLM MLHR MLAB MKTX MKSI MKL MJN MITSY MITL MITK MIME MIK MIDD MIC MHK MGPI MGM MGA MFSF MFS MFC MET MEOH MENT MEMP MELI MEI MEET MDU MDT MDSO MDLZ MDCO MDC MD MCRI MCO MCK MCHP MCD MCBC MBWM MBT MBLY MBFI MBFI MATW MAT MASI MAS MAR MANH MAN MAG MAC MAA MA M LZB LYV LYB LXK LWAY LVS LVMUY LVLT LUX LUV LUNMF LULU LUK LTXB LTC LSXMA LSTR LRCX LPX LPT LPNT LPLA LPL LOGI LOCK LNVGY LNT LNKD LNG LNDNF LNCE LNC LN LMT LMNR LM LLTC LLL LL LKQ LKFN LIVN LITE LHCG LH LGND LGF LFUS LFL LEN LEG LECO LEA LDOS LC LBTYA LB LAZ LANC LAMR LAD LABL L KT KSU KS KRNY KRC KR KORS KOF KO KNYJF KNX KNL KMX KMB KLAC KIM KHC KGC KFY KEYS KEY KEX KEP KCRPY KBH KATE KAR KALU KAI K JWN JW/A JUNO JPM JOY JNS JNPR JNJ JMPLY JLL JKHY JJSF JEC JD JCP JCOM JCI JBT JBLU JBL JBHT JAZZ JACK IVZ ITW ITUB ITT ITRI ITOCY ITI ITGR ITCB ITC IT ISRG ISLE ISIL ISBC IRBT IR IQNT IPPLF IPHS IPGP IPG IP IOSP IONS INXN INTU INT INST INOV INGR INFY INFN INDB INCY IMS IMO IMAX ILMN IILG IGT IFF IEX IEP IDXX IDTI IDCC IDA ICUI ICPT ICLR ICE IBTX IBP IBM IBKR IBKC IBA IART IAG IAC HZO HZN HYH HXL HW HURN HUN HUM HUBS HUBB HTHT HTHIY HTH HTBK HTA HSY HST HSNI HSKA HSIC HRS HRL HRI HRC HRB HR HQY HPT HPQ HPP HPE HP HOWWY HOV HON HOMB HOLX HOLI HOKCY HOG HOFT HNRG HNI HMY HMN HMC HLX HLT HLS HLF HL HIW HII HIG HIBB HHC HFWA HFC HES HEP HE HDS HDP HDB HD HCSG HCP HCN HCCI HBI HBHC HBAN HASI HAS HAR HAL HAIN HAFC HAE HA H GXP GWW GWRE GWR GWPH GTN GT GSS GSK GSBC GSAT GS GRUB GRPN GRMN GRFS GRC GRA GPT GPRO GPRE GPP GPOR GPN GPK GPC GOOG GOLD GOGO GNTX GNC GMS GMED GME GMBXF GLW GLPI GLOB GLNG GKOS GK GIS GIMO GILD GIL GIB GGP GGG GGB GGAL GG GFI GES GEL GEF GE GDEN GDDY GD GCP GBDC GBCI GATX G FWRD FUN FULT FUL FUJIY FTR FTNT FTK FTI FSV FSS FSM FRT FRO FRME FRAN FR FOXA FOX FOSL FOE FNV FNSR FNF FNB FN FMX FMS FMC FMBI FLTX FLT FLS FLR FLO FLIR FLIC FLEX FL FIX FIVN FITB FIT FISV FIS FICO FIBK FI FGP FFNW FFIV FFG FEYE FELE FE FDX FDP FDML FDEF FDC FCX FCREY FCF FCE/A FCCY FCBC FCAU FBR FBHS FBC FB FAST FARM FANG FAF F EZPW EXR EXPO EXPE EXPD EXLS EXK EXEL EXC EXAS EXAR EWBC EW EVTCY EVR EVHC EVH EVER EVC EV ETSY ETR ETN ETH ETFC ETE ESS ESRX ESGR ES EROS ERII ERIE ERIC ERI EQY EQT EQR EQM EQIX EQGP EQC EPR EPD EPC EOG EOCC ENZ ENV ENTG ENS ENR ENLK ENH ENGIY ENDP ENBL ENB EMR EMN EME ELY ELS ELLI EL EIX EIG EGP EGOV EGO EGHT EGBN EFX EFII EEP EEFT EE EDU EDR ED ECR ECL ECA EC EBAY EA DY DXPE DXCM DW DVN DVA DV DUK DTSI DTSI DTLK DTE DSGX DRQ DRI DRE DPZ DPS DPM DOX DOW DORM DOOR DOC DO DNOW DNKN DNB DM DLX DLTR DLPH DLB DLAKY DKS DISH DISCK DISCA DIS DHR DHI DGX DGI DFT DF DEO DENN DEI DECK DE DDAIF DD DCT DCO DCM DCI DBVT DB DATA DAR DAL DAKT CYNO CY CXW CXO CWST CW CVX CVLT CVGW CVG CVE CVCO CVA CUBI CUBE CTXS CTWS CTSH CTRP CTLT CTB CTAS CSX CSWI CST CSRA CSL CSGS CSGP CSFL CSC CSAL CRY CRUS CROX CRMT CRM CRL CRI CRH CREE CRC CRAY CR CQP CQH CPS CPRT CPPL CPN CPK CPF CPCAY CPB CPA CP COTY COT COST CORE COR COP COO CONN CONE COMM COLM COL COHR COH COG COF CODI CNXC CNX CNS CNQ CNP CNO CNK CNI CNHI CNCO CNC CMPR CMPGY CMN CMI CMG CMCSA CMCO CMA CM CLX CLW CLR CLNE CLI CLH CLGX CLC CLB CL CKEC CIVB CIT CINF CIM CIB CI CHUY CHTR CHS CHRW CHKP CHK CHH CHGG CHFC CHEF CHE CHDN CHD CGNX CG CFX CFR CFI CFG CF CERN CENT CEMP CELG CEB CE CDW CDNS CDK CDE CCP CCOI CCO CCK CCJ CCE CBU CBS CBRL CBOE CBM CBI CBG CBD CB CAVM CATY CATM CAT CASY CARB CALX CALM CAKE CAH CAFD CAE CACI CACC CABO CAA CA C BZLFY BYD BXS BXP BX BWXT BWP BWLD BWA BURL BUFF BUD BTX BTG BSX BSMX BSBR BSAC BRX BRSS BRS BRO BRKR BRK/B BRK/A BRFS BRC BR BPOP BPL BPFH BPFH BOX BOKF BOH BOFI BNS BNCN BNCL BMY BMTC BMS BMRN BMO BMI BMA BLMN BLL BLKB BLK BLD BLBD BKS BKH BKFS BK BJRI BITA BIP BIO BIIB BIG BIDU BID BHP BHLB BHI BGS BGG BG BFR BFIN BF/B BERY BEP BEN BECN BEAV BEAT BDX BDVSY BDRBF BDC BCR BCH BCE BCC BC BBY BBT BBD BBBY BAX BASFY BAP BANR BAH BAC BABY BABA BA AZPN AZO AZN AYI AXTI AXTA AXS AXP AXON AXGN AXE AWR AWK AWI AWH AVY AVT AVGO AVG AVD AVB AVA AUY AUO AU ATVI ATR ATO ATNI ATI ATHN ATHM ASX ASTE ASR ASNA ASML ASH ASGN ASB ASAZY ARW ARTNA ARRS ARMK ARGKF ARCC AR APU APOG APO APH APD APC APA AOSL AOS AON ANW ANTM ANSS ANIK ANF ANET ANCX ANCUF AN AMZN AMX AMWD AMTD AMT AMSG AMP AMKR AMH AMGN AMG AMED AME AMD AMCX AMBA AMAT AM ALXN ALV ALSN ALR ALOG ALNY ALLY ALLE ALL ALKS ALK ALIOF ALGN ALE ALB AL AKS AKRX AKR AKO/B AKAM AJG AIZ AIV AIR AIN AIG AHS AHL AHH AHGP AGX AGU AGR AGO AGNC AGN AGII AGI AGCO AG AFSI AFL AFG AF AET AES AER AEP AEO AEM AEIS AEE ADSK ADS ADPT ADP ADM ADI ADDYY ADBE ACTG ACN ACIW ACIA ACGL ACC ACAT ACAS ACAD ABX ABTX ABT ABMD ABM ABG ABEV ABCO ABCB ABC ABBV ABB AAT AAPL AAP AAL AA A "
  val indexes = Map("DJI" -> "https://www.quandl.com/api/v3/datasets/YAHOO/INDEX_DJI.json?api_key=Xmky6espzDoofkY9CFar")
  test("test1") {
    val currentQuoteFile = "/Users/nishchaysinha/stocksdatadir/currentPerformance/processed/Securities_to_Watchdatestart2016-10-06T20:22:28Zdateend.csv"
    val quoteImpl = new CsvDailyQuotesScottradeProjectImpl(modelFilePath = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/modelforcsv.txt", quotesFilePathInput = currentQuoteFile , classzz = GenCsvQuoteRowScottrade.getClass)
    quoteImpl.appendDataToYearFile("/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/metafile2016.txt","/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/2016-aggregate-data.txt")
    val topFlowers = quoteImpl.writeTopFlowersForToday(10)
    topFlowers.toString() should be ("List(GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,CORE,Price(45.11),Price(38.72),Price(44.15),Price(44.15),Price(37.16),Volume(3083014.0),CORE MARK HOLDING COMPANY INCORPORATED,Percent(14.17)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,EDIT,Price(18.33),Price(17.42),Price(18.16),Price(18.2329),Price(16.56),Volume(398374.0),EDITAS MEDICINE INCORPORATED,Percent(4.96)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,GFI,Price(5.8),Price(5.55),Price(5.66),Price(5.69),Price(5.53),Volume(5713647.0),GOLD FIELDS LTD,Percent(4.31)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,CXW,Price(17.51),Price(16.79),Price(17.68),Price(18.14),Price(15.07),Volume(1.1253477E7),CORRECTIONS CORPORATION OF AMERICA,Percent(4.11)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,VIRT,Price(17.4),Price(16.8),Price(17.4),Price(17.4),Price(16.78),Volume(534489.0),VIRTU FINANCIAL INC,Percent(3.45)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,BRSS,Price(29.68),Price(28.75),Price(29.63),Price(29.94),Price(28.61),Volume(133365.0),GLOBAL BRASS AND COPPER HOLDINGS INC,Percent(3.13)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,FDML,Price(9.29),Price(9.04),Price(9.37),Price(9.4068),Price(8.93),Volume(179206.0),FEDERALMOGUL HOLDINGS CORPORATION,Percent(2.69)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,SCTY,Price(22.05),Price(21.46),Price(22.12),Price(22.2),Price(21.41),Volume(2432962.0),SOLARCITY CORPORATION,Percent(2.68)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,OCANF,Price(3.43),Price(3.34),Price(3.37),Price(3.43),Price(3.33),Volume(70150.0),OCEANAGOLD CORPORATION,Percent(2.62)), GenCsvQuoteRowScottrade(167253297255000000,167253297278400000,ISIL,Price(19.13),Price(18.65),Price(19.0),Price(19.13),Price(18.63),Volume(2290542.0),INTERSIL CORPORATION,Percent(2.51)))")
    logger.info(topFlowers.toString())
  }

  test("orderAnalysis") {
    val quoteFile = "/Users/nishchaysinha/Downloads/Securities_to_Watch2016.11.02.10.26.39.csv"
    val orderFile = "/Users/nishchaysinha/Downloads/CompletedOrders2016.11.02.11.20.28.csv"
    OrderAnalysis.OrderAnalysis(quoteFile,orderFile)
  }

  test("csvtest2") {
    val jsonFileName = "/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/testCombine"
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    println(str)
    fw.write(str)
    fw.close()
  }

  test("testYearlyTS"){
    val analysisProject = new YearlyQuoteAnalysisProjectImpl("/Users/nishchaysinha/nsinhamisc/stocks/src/test/resources/2016-aggregate-data.txt")
    val str = analysisProject.createTimeSeries[Price](axisString = "endprice", canBuildT =  new Price(0))
    logger.info(str)
  }


  test("process all quotes") {
     val consumeAllQuotes = new ConsumeAllScottradeQuotes("/Users/nishchaysinha/stocksdatadir/currentPerformance")
     consumeAllQuotes()
  }

  test("process closing price") {
    val jsonFileName = "/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/closingprice.json"
    val clpTsClazz = new ClosingPriceTimeSeries("/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/combinedData.json")
    val ts = clpTsClazz.getTransformed
    implicit val format = DefaultFormats
    val jsonStr = writePretty(ts)
    FileUtils.writeFile(jsonFileName, jsonStr)
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToTsCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    fw.write(str)
    fw.close()
  }

  test("process normalized closing price with filtering") {
    val clpTsObject = new ClosingPriceNormalizedTimeSeries("/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/combinedData.json")
    val ts = clpTsObject.getTransformed
    val ts_winnersFor15days  = PriceNormalizedTimeSeries.getTheWinnersPastIntervals(ts, 15)
    val ts_winnersFor30days  = PriceNormalizedTimeSeries.getTheWinnersPastIntervals(ts, 30)

    implicit val format = DefaultFormats
    val jsonStr = writePretty(ts)
    val jsonFileName = "/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/closingpricenormalized.json"
    val jsonFileName15days = jsonFileName + ".15days.json"
    val jsonFileName30days = jsonFileName + ".30days.json"
    FileUtils.writeFile(jsonFileName, jsonStr)
    FileUtils.writeFile(jsonFileName15days, writePretty(ts_winnersFor15days))
    FileUtils.writeFile(jsonFileName30days, writePretty(ts_winnersFor30days) )
    writeCsvFile(jsonFileName)
    writeCsvFile(jsonFileName15days)
    writeCsvFile(jsonFileName30days)
  }

  def writeCsvFile(jsonFileName: String) = {
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToTsCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    fw.write(str)
    fw.close()
  }



  test("low price") {
    val cl_lp_TsObject = new LowPriceNormalizedTimeSeries("/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/combinedData.json")
    val ts = cl_lp_TsObject.get
    implicit val format = DefaultFormats
    val jsonStr = writePretty(ts)
    val jsonFileName = "/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/lowprice.json"
    FileUtils.writeFile(jsonFileName, jsonStr)
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToTsCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    fw.write(str)
    fw.close()
  }

  test("low price normalized") {
    val cl_lp_TsObject = new LowPriceNormalizedTimeSeries("/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/combinedData.json")
    val ts = cl_lp_TsObject.getTransformed
    implicit val format = DefaultFormats
    val jsonStr = writePretty(ts)
    val jsonFileName = "/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/lowpricenormalized.json"
    FileUtils.writeFile(jsonFileName, jsonStr)
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToTsCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    fw.write(str)
    fw.close()
  }

  test("low price to closing normalized") {
    val cl_lp_TsObject = new LowPriceToClosingNormalizedTimeSeries("/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/combinedData.json")
    val ts = cl_lp_TsObject.getTransformed
    implicit val format = DefaultFormats
    val jsonStr = writePretty(ts)
    val jsonFileName = "/Users/nishchaysinha/stocksdatadir/currentPerformance/output/yearly/2016/lowpricetoclosingpricenormalized.json"
    FileUtils.writeFile(jsonFileName, jsonStr)
    val jsonCsv = new JsonCsvProjectImpl(modelFile = "" , jsonFile = jsonFileName, csvFile = "")
    val str = jsonCsv.changeAJsonToTsCsv()
    val fw = new FileWriter(jsonFileName + ".csv")
    fw.write(str)
    fw.close()
  }


  test("Quandl yearly quotes") {
    val tickersArray = tickers.split(" |\n|\r")
    val fut = tickersArray map (ticker => QuandlOHLCDump.QuoteForTickerFromQuandl.createYearlyFilesForTicker(ticker, "/Users/nishchaysinha/stocksdatadir/ohlc/yearlies", "https://www.quandl.com/api/v3/datasets/WIKI"))
    do {}while(true)
  }

  test("Quandl indexes yearly quotes") {
    val fut = indexes map (kv => QuandlOHLCDump.QuoteForTickerFromQuandl.createYearlyFilesForMaps(kv._1, "/Users/nishchaysinha/stocksdatadir/ohlc/yearlies",kv._2))
    do {}while(true)
  }

  test("Accumulate Quandl yearly quotes for various tickers") {
    for (year <- Range(1985,2017)) {
      ConcatenateJsonFiles.processDirectory(s"/Users/nishchaysinha/stocksdatadir/ohlc/yearlies/${year}", outputfile = "combinedData.json")
    }
  }

  def `process  closing price for many years` =  {
    ClosingPriceRecords.processYears(Range(1985, 2017).toList)
  }

  def `process  open price for many years` =  {
    OpenPriceRecords.processYears(Range(1985, 2017).toList)
  }

  def `process  low price for many years` =  {
    LowPriceRecords.processYears(Range(1985, 2017).toList)
  }

  def `process  high price for many years` = {
    HighPriceRecords.processYears(Range(1985, 2017).toList)
  }

  def `process  volume for many years` = {
    VolumeRecords.processYears(Range(1985, 2017).toList)
  }

  test("run all ohlc into a csv file")  {
    YearlyRecords.processOHLCForYears(Range(1985,2017).toList)
  }

}
