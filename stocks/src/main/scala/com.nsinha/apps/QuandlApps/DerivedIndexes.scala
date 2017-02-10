package com.nsinha.apps.QuandlApps

import com.nsinha.impls.Project.QuandlOHLCDump.DerivedIndex.DerivedNormalizedIndex
import com.nsinha.impls.Project.QuandlOHLCDump.NormalizedOHLC.NormalizeTickers

/**
  * Created by nishchaysinha on 11/15/16.
  */
object DerivedIndexes {
  val INDXCONSUMERDISC= "AAP,AMZN,AN,AZO,BABA,BBBY,BBY,BWA,KMX,CCL,CBS,CHTR,CMG,COH,CMCSA,DHI,DRI,DLPH,DISCA,DISCK,DG,DLTR,EXPE,FL,F,GPS,GRMN,GM,GPC,GT,HBI,HOG,HAR,HAS,HD,IPG,JCI,KSS,LB,LEN,LKQ,LOW,M,MAR,MAT,MCD,KORS,MHK,NWL,NWSA,NWS,NKE,JWN,ORLY,OMC,RL,PCLN,PHM,PVH,ROST,RCL,SNI,SIG,SNA,SWK,SPLS,SBUX,TGT,TGNA,TIF,TWX,TJX,TSCO,TRIP,FOXA,FOX,ULTA,UA,UA.C,URBN,VFC,VIAB,DIS,WHR,WYN,WYNN,YUM"
  val INDXMEDIA="CMCSA,DIS,TWX,FOXA,WPPGY,DISH,CBS,VIVHY,SIRI,OMC,SKYAY,NLSN,PUBGY,VIAB,IPG,SNI,LBTYA,PSO,LAMR,LYV,TGNA"
  val INDXOTHERSPECIAL="HDPT,LOW,ROST,ORLY,LUX,AZO,LB,ULTA,BBY,AAP,COH,KMX,KGFHY,GPS,TSCO,FL,TIF,KORS,BBBY,MAKSY,SIG,DKS,SPLS.CPRT,IM"
  val INDXTEXTILE="LVMUY,NKE,ADDYY,VFC,HBI,RL,PVH,LULU,UA,GIL,COLM,SKX,WWW,SHOO,DECK,FOSL"
  val INDXAUTO = "TMH,DDAIF,HMC,GM,F,VLKAY,NSNY,CTTAY,DNZOY,FUJHY,BRDCY,TSLA,CMI,TTM,DLPH,MGA,GPC,VLEEY,HOG,PUGOY,RACE,LKQ,FCAU,LEA,LIV"
  val INDXDIVRETAIL = "AMZN,COST,TJX,TGT,WMMVY,DG,DLTR,M,JWN,QVCA,KSS,VIPS,BURL,LVNTA,MAURY,JCP,W,PSMT,DDS,BIG,GRPN,FIVE,HSNI,GPGJY,SHLD"
  val INDXHOMEBUILD="CODGF,DWAHY,WOSYY,AYI,SKHSY,MAS,DHI,FBHS,LEN,PHM,OC,NVR,FCREY,TOL,CAA,VMI,BECN,TMHC,AWI,SSD,TREX,DOOR,TPH,HW,MTH"
  val INDXHOTELENT="MCD,SBUX,PCLN,LVS,MAR,CMPGY,CCL,CMPGF,HLT,YUM,KYNC,CTRP,EXPE,RCL,MGM,SDXAY,CMG,YUMC,QSR,MPEL,CUK,WYNN,NCLH,DRI,ARMK,IHG,DPZ,WYN,GIGNY,TRIP,H,GMALY"
  val INDXHOUSEHOLDGOODS = "NWL,MHK,WHR,MIDD,LEG,ELUXY,HRELF,TPX,HSQVY,HRG,TUP,HEL,LZB,TILE,SCSS,ETH,DIIBF,NC,FLXS,LBY,BSET,HOFT,WEDXF,LCUT"
  val INDXLEISUREPRODS = "NTDOY,ATVI,EA,HAS,MAT,YAMCY,NINOY,PII,KNMCY,THO,POOL,TTWO,UBSF,AGPDY,DW"
  val INDXBEVERAGE="KO,BUD,PEP,ABEV,DEO,HEINY,STZ,MNST,TAP,FMX,CCE,DPS,KNBWY,BF/B,CABGY"
  val INDXFOODDRUGS="WMT,WBA,CVS,ESRX,MCK,KR,SYY,ADRNY,CAH,TSCDY,ANCUF,ABC,CRERF,PUSH,JRONY,WFM,CNCO,RAD,CRHKY,JSAIY"
  val INDXFOODTOB="NSRGY,PM,MO,KHC,BTI,RAI,MDLZ,MON,IMBBY,DANOY,GIS,K,ASBFY,ADM,TSN,HSY,HRL,CPB,CAG,SJM,MJN,BRFS,KRYAY,GRBMF,AJINY,MKC,WWAV,IFF,BG,ORKLY,INGR,PF,GLAPY,LW,TBLMY,POST,PPC,SEB,BUFF,TATYY,HAIN"
  val INDXPERSONAL="PG,LRLCY,UN,RBGLY,CL,UL,KMB,EL,KCRPY,SVCBY,CLX,COTY,CHD,SSDOY,XRS,HRB,SCI,EPC,HLF,BFAM,NUS,KCDMY,GHC,LOPE"
  val INDXCONSUMERSTAPLES = "MO,ADM,BF-B,CPB,CHD,CLX,KO,CL,CAG,STZ,COST,CVS,DPS,EL,GIS,HRL,SJM,K,KMB,KHC,KR,MKC,MJN,TAP,MDLZ,MNST,PEP,PM,PG,RAI,SYY,HSY,TSN,WMT,WBA,WFM"



  val INDXBANKS = "BSBR, BBD,AFL,AMG,ALL,AXP,AMP,AON,AJG,AIZ,BAC,BK,BBT,BRK-B,BLK,HRB,COF,SCHW,CB,CINF,C,CFG,CME,CMA,DFS,ETFC,EFX,FITB,BEN,GS,HIG,HBAN,ICE,IVZ,JPM,KEY,LM,LUK,LNC,L,MTB,MMC,MET,MCO,MS,NDAQ,NAVI,NTRS,PBCT,PNC,PFG,PGR,PRU,RF,STT,STI,SYF,TROW,TRV,TMK,USB,UNM,WFC,WLTW,XL,ZION" //Airline freight
  val INDXINSURANCE="BRK/A,AZSEY,AIG,MET,CB,XAHY,PUK,PRU,ZFSVF,ZURVY,MMC,MFC,TRV,SSREY,TKOMY,AFL"
  val INDXFINANCIALS ="ACAS,ACGL,AFSI,AGII,AGNC,AMTD,ANAT,BANR,BGCP,BOFI,BOKF,BPOP,CACC,CATY,CBOE,CBSH,CFFN,CHFC,CIGI,CINF,CMCT,CME,COLB,CONE,CSAL,CVBF,EQIX,ERIE,ESGR,ETFC,EWBC,FCNCA,FFBC,FFIN,FITB,FMBI,FNGN,FSV,FULT,GBCI,GLPI,HAWK,HBAN,HBHC,HOMB,HOPE,HQY,IBKC,IBKR,IBOC,INDB,ISBC,LAMR,LBRDK,LPLA,LTRPA,LTXB,MBFI,MKTX,NAVG,NAVI,NBTB,NDAQ,NGHC,NTRS,NWBI,ONB,OZRK,PACW,PBCT,PCH,PNFP,PRAA,PVTB,RNST,ROIC,SBNY,SBRA,SEIC,SFBS,SFNC,SIGI,SIVB,SLM,SSB,TCBI,TFSL,TRMK,TROW,UBSI,UCBI,UMBF,UMPQ,WABC,WAFD,WETF,WLTW,WTFC,ZG,ZION"
  val INDXRE = "AIV,CBG,CCI,DLR,EQR,ESS,EXR,FRT,GGP,HCP,HST,IRM,KIM,MAC,PLD,O,SLG,UDR,VTR,HCN,SUHJY,CAOVY,MITEY,HLDCY,WHLKY,NDVLY,HLPPY,SPG,AMT,PSA,CCI,PLD,EQIX,WY,AVB,BXP,VNO"

  val INDXHEALTH = "ABT,ABBV,AET,A,AGN,ALXN,ABC,AMGN,ANTM,BCR,BAX,BDX,BIIB,BSX,BMY,CAH,HSIC,CELG,CNC,CERN,CI,DVA,XRAY,EW,ENDP,ESRX,GILD,HCA,HOLX,HUM,ILMN,ISRG,JNJ,LH,LLY,MNK,MCK,MDT,MRK,MTD,MYL,PDCO,PKI,PRGO,PFE,DGX,REGN,STJ,SYK,COO,TMO,UNH,VAR,VRTX,WAT,ZBH,ZTS"
  val INDXHEALTHEQUIP="MDT,TMO,DHR,SYK,BDX,BSX,PHG,BAX,ISRG,ESLOY,STJ,ZBH,ILMN,EW,CERN,BCR,HOCPY,A"
  val INDXHEALTHPROVIDERS ="UNH,AET,FSNUF,ANTM,CI,HUM,HCA,FMS,LH,DV,UHS,DHX,CNC"
  val INDXMEDRESEARCH="GILD,AMGN,CELG,REGN,VRTX,Q,INCY,ALIOF,SGEN,ALKS,TSRO,QGEN,UTHR,IONS,EXEL"
  val INDXBIO = "ALXN,AMGN,BIIB,CELG,GILD,REGN,VRTX,ENDP,RGNX"

  val INDXINDUSTRIAL = "MITCY,ITOCY,KAJMY,MMM,AYI,ALK,ALLE,AAL,AME,APH,ARNC,BA,CHRW,CAT,CTAS,GLW,CSX,CMI,DHR,DE,DAL,DOV,DNB,ETN,EMR,EXPD,FAST,FDX,FLIR,FLS,FLR,FTV,FBHS,GD,GE,GWW,HON,ITW,IR,JEC,JBHT,KSU,LLL,LEG,LMT,MAS,NLSN,NSC,NOC,PCAR,PH,PNR,PBI,PWR,RTN,RSG,RHI,ROK,COL,ROP,R,LUV,SRCL,TXT,TDG,UNP,UAL,UPS,UTX,VRSK,WM,XYL,BIP,MIC"
  val INDXAEROSPACE= "BA,UTX,LM,GD,NOC,RTN,BAESY,THLEY,RYCEY,TDG,TXT,LLL,COL,HII,SPR,BEAV,OA,HXL,ESLT,CW,BWXT"
  val INDXLOGISTICS= "UPS,UNP,FDX,CNI,DPSGY,CNX,ZTO,JBHT,XPO,R"
  val INDXINDUSCONGLOMERATES="GE,MMM,SIEGY,HON,DD,ITW,JCI,MSBHY,MIELY,HTHIY,TOSYY,SMEBF,GPOVY,CSL,SMGZY,KWHIY"
  val INDXMACHINERYEQUIP="CAT,ABB,EMR,ETN,DE,NJDCY,ATLKY,TEL,KMTUY,PCAR,KNYJF,SMECF,IR,KUBTY,SWK,ROP,PH,KYO,ROK,SDVKY,GWW,FAST,AME,DOV,MTD,CNHI,PNR,SNA,XYL"
  val INDXTRANSPORTATION ="JBHT,CHRW,KSU,UPS,MATX,LSTR,KEX,R,EXPD,FDX,LUV,UNP,NSC,CSX,CAR,DAL,MTRJY,ICAGY,AAL,UAL,RYAAY,ALNPY,ALK,JBLU,DLAKY,CPCAY,LFL"
  val INDXHOSPITALITY="MCD,SBUX,PCLN,LVS,MAR,CCL,CMPGY,HLT,YUM,KYNC,CTRP,EXPE,RCL,MGM,SDXY,CMG,QSR"


  val INDXIT = "CARB,MIME,ACN,ATVI,ADBE,AKAM,ADS,DATA,GOOGL,GOOG,AAPL,ADSK,ADP,AMAT,AVGO,CSCO,INTC,CTXS,CTSH,CSRA,EBAY,EA,FFIV,FB,FIS,FSLR,FISV,GPN,HRS,HPE,HPQ,IBM,INTU,JNPR,KLAC,LRCX,LLTC,MA,MCHP,MU,MSFT,MSI,NTAP,NFLX,NVDA,ORCL,PAYX,PYPL,QRVO,QCOM,RHT,CRM,STX,SWKS,SYMC,TEL,TDC,TXN,TSS,VRSN,V,WDC,WU,XRX,XLNX,YHOO"
  val INDXCOMPUTERS="AAPL,HPE,SNE,HPQ,NOK,PCRFY,WDC,DVMT,STX,NTAP,SHCAY,LNVGY,SNX,LOGI,GLW,APH,LPL,ALLE,ST,AVT,TDY"
  val INDXOFFICEEQUIP="CAJ,FUJIY,RICOY,KNCAY,PBI"
  val INDXSEMI = "AMD,CSCO,QCOM,STM,SNPS,CDNS,IIVI,INTC,TSM,TXN,AVGO,NVDA,ASML,NXPI,AMAT,ADI,MU,IFNNY,FTV,LRCX,SWKS,LLTC,MCHP,XLNX,KLAC,RNECY,MXIM,STM,ASX,MRVL,FLEX"
  val INDXSOFTWARE = "MSFT,FB,GOOGL,TCTZF,BBA,ORCL,IBM,SAP,ACN,NPSNY,BIDU,CRM,ADBE,NFLX,PYPL,JD,CTSH,VMW,EBAY,INFY,NTES,INTU,LNKD,WIT,DASTY,ADSK,WDAY,SYMC,PANW,CHKP,RHT,NOW,VRSK,CGEMY,CTXS,TWTR,CA,GIB,FJTSY,AKAM,WB,FFIV,TSS,SNPS,CSC,DOX"

  val INDXTELCOM = "T,CHL,CTL,FTR,LVLT,VZ,IDT,DCM,NTT,CHTR,DTEGY,VOD,SFTBF,KDDIF,BT,TLSYY,TMUS,TEF,ORAN,BCE,S,TLK,CHU,LVLT"

  val INDXUTILITIES = "ES,LNT,AEE,AEP,AWK,CNP,CMS,ED,D,DTE,DUK,EIX,ETR,ES,EXC,FE,NEE,NI,NRG,PCG,PNW,PPL,PEG,SCG,SRE,SO,WEC,XEL"

  val INDXSTARTUPS = "MSBI,NCBS,LN,GCP,MFS,NTLA,VLVLY,LSXMA,RETA,GMS,SUPV,NGVT,COTV,AFI,TPB,SCWX,SNDX,PTI,TWLO,BATRA,SENS,BATRK,NTNX,USFD,EDIT,GWRS,WINS,YIN,CRVS,QHC,SITE,AVXS,LSXMK,RRR,LIND,ACIA,ARA"

  val INDXENERGY = "PBR,PTEN,APC,APA,BHI,COG,CHK,CVX,XEC,CXO,COP,DVN,EOG,EQT,XOM,FTI,HAL,HP,HES,KMI,MRO,MPC,MUR,NOV,NFX,NBL,OXY,OKE,PSX,PXD,RRC,SLB,SWN,SE,TSO,RIG,VLO,WMB"
  val INDXCOAL ="CSUAY,CC,EXXAY"
  val INXDOILGAS="XOM,CVX,TOT,RDS/A,BP,CEO,COP,EOG,OGZPY,STO,OXY,SU,E,PSX,PBR,CNQ,APC,LUKOY,NOVKY,PXD,VLO,IMO,APA,DVN,MPC"
  val INDXOILGASEQUIP = "SLB,EPD,KMI,HAL,ENB,TRP,SE,BHI,WMB,ETP,ETE,TS,MMP,NOV,SEP,PAA,MPLX,OKX,PBA,CQP,TRGP,BPL,LNG,SXL,TKPPY,PAGP,EEP,FTI,HP,ENBL,EQM,ENLK,AM,TLLP"
  val INDXRENWABLE="GCTAF,FSLR,SCTY,TSL,GPRE,SPWR,AJGH,HQCL"
  val INDXURANIUM= "CCJ,DNN,UEC,UUUU,URG,BNNLF,URRE"

  val INDXMATERIALS = "TECK,D,ALB,AVY,BLL,CF,DOW,DD,EMN,ECL,FMC,FCX,IP,IFF,LYB,MLM,MON,MOS,NEM,NUE,OI,PPG,PX,SEE,SHW,VMC,WRK"
  val INDXMETALS = "BHP,RIO,NILSY,SCCO,VALE,GMBXF,NGLOY,NUE,FCX,ABX,NM,PKX,AM,CISXF,TCK,NCMGY,GG,FNV,AEM,NHYDY,OPYGY,VEDL,ARNC,STLD,SLW"
  val INDXMATPACKAGING = "IP,BLL,WRK,AMRCRY,SEE,PKG,CCK,AVY,BERRY,SON,ATR,BMS,GPK,OI,SLGN"
  val INDXMATERIALSBUILD= "HCMLY, CRH, VMC, MMM, CX,JHX, EXP, USG,  BOALY, SUM, WBRBY, GCP, CEMTY, FRTA, PGEM, CPAC"
  val INDXCHEM = "CC,DOW,BASF, AIQUY, SYT, LYB, PX, APD, PPG, SHW, HENOY, AKZOY,POT,AGU,CE, EMN,MOS"
  val INDXPAPER="UPMKY,SEOAY,KLBAY,FBR,SPPJY"

  val derivedIndexes :Map[String, String] = Map("INDXCONSUMERDISC" -> INDXCONSUMERDISC,"INDXMEDIA"-> INDXMEDIA, "INDXOTHERSPECIAL" -> INDXOTHERSPECIAL,
    "INDXTEXTILE"->INDXTEXTILE, "INDXCONSUMERSTAPLES" -> INDXCONSUMERSTAPLES,"INDXDIVRETAIL "->INDXDIVRETAIL ,"INDXHOMEBUILD"-> INDXHOMEBUILD,
    "INDXHOTELENT"->INDXHOTELENT,"INDXHOUSEHOLDGOODS"-> INDXHOUSEHOLDGOODS, "INDXPERSONAL"->INDXPERSONAL,"INDXFOODTOB"->INDXFOODTOB,
    "INDXFOODDRUGS"->INDXFOODDRUGS,"INDXBEVERAGE"->INDXBEVERAGE,"INDXLEISUREPRODS"->INDXLEISUREPRODS,  "INDXENERGY" ->  INDXENERGY,
    "INDXCOAL" -> INDXCOAL, "INXDOILGAS" -> INXDOILGAS,"INDXOILGASEQUIP" -> INDXOILGASEQUIP,"INDXRENWABLE"->INDXRENWABLE,"INDXURANIUM"->INDXURANIUM,
    "INDXFINANCE" ->INDXBANKS, "INDXMEDRESEARCH" -> INDXMEDRESEARCH,"INDXHEALTHPROVIDERS" -> INDXHEALTHPROVIDERS,  "INDXHEALTHEQUIP"->INDXHEALTHEQUIP,
    "INDXHEALTH" -> INDXHEALTH, "INDXINDUSTRIAL" -> INDXINDUSTRIAL,"INDXINSURANCE"->INDXINSURANCE, "INDXAEROSPACE" ->INDXAEROSPACE,"INDXLOGISTICS" -> INDXLOGISTICS,
    "INDXINDUSCONGLOMERATES"->INDXINDUSCONGLOMERATES, "INDXMACHINERYEQUIP"->INDXMACHINERYEQUIP, "INDXIT" -> INDXIT, "INDXSEMI" -> INDXSEMI,
    "INDXCOMPUTERS"->INDXCOMPUTERS,"INDXOFFICEEQUIP"->INDXOFFICEEQUIP,"INDXSOFTWARE"-> INDXSOFTWARE, "INDXMATERIALS" -> INDXMATERIALS, "INDXMETALS"-> INDXMETALS,
    "INDXMATPACKAGING" ->INDXMATPACKAGING, "INDXMATERIALSBUILD" -> INDXMATERIALSBUILD,"INDXCHEM" -> INDXCHEM,"INDXPAPER" -> INDXPAPER,
    "INDXAUTO" -> INDXAUTO, "INDXRE" -> INDXRE, "INDXBIO" -> INDXBIO, "INDXTRANSPORTATION" -> INDXTRANSPORTATION, "INDXFINANCIALS" -> INDXFINANCIALS,
    "UINDXHOSPITALITY" -> INDXHOSPITALITY, "INDXLOGISTICS" -> INDXLOGISTICS, "INDXCHEM" -> INDXCHEM,"INDXUTILITIES" -> INDXUTILITIES, "INDXTELCOM" -> INDXTELCOM,
    "INDXSTARTUPS" -> INDXSTARTUPS)

  def run() = {
    for (year <- List(2016)) {
      derivedIndexes map {idxKV => DerivedNormalizedIndex.processDirectory(
        s"/Users/nishchaysinha/stocksdatadir/ohlc/yearlies/${year}/normalized", listOfTickersInIndex = commaseparatedIntoList(idxKV._2), index = idxKV._1 ) }
    }
  }

  private def commaseparatedIntoList(s: String): List[String] = {
    s.split(",") toList
  }

  def main(args: Array[String]) {
    run()
  }




}