#! /bin/sh
# script for nightly builds.
# you only need to have this script locally, it will fetch sources from cvs, compile them, generate the website and upload everything.
# IT WILL DOWNLOAD EVERYTHING TO "jtidy" AND DELETE SUCH DIRECTORY WHEN FINISHED!

CVSREPO=:pserver:anonymous:@cvs1.sourceforge.net:/cvsroot/jtidy
DATE=`date`
. .shrc

mkdir jtidy
cd jtidy

echo "*** getting sources from cvs ***"
cvs -d $CVSREPO login 
cvs -Q -d $CVSREPO co all 



cd jtidy

echo "*** start build ***"
maven -eo nightly


echo "*** removing work dir ***"
cd ../..
rm -Rf jtidy
