#!/bin/bash

getStat()
{
	for i in 'descending' 'even-odd' 'fibonacci' 'palindrome' 'prime' 
	do
		echo 'for program '$i'.out'

		ant -q
		ant make-jar -q
		ass1prog='../ass1_programs/'$i'.out'
		echo $ass1prog
		tempStat='temp.stat'
		jarFile='jars/simulator.jar'
		tempRegister='temp.register'

		touch $tempStat
		java -jar $jarFile $configFile $tempStat $ass1prog >> $tempRegister
		
		stat=$(<$tempStat)
		line=$i','$stat','$liLatency','$liCacheSize','$ldLatency','$ldCacheSize
		echo $line
		echo $line >> $statFile
		rm -f $tempStat
		rm -rf 'jars'
		rm -rf 'bin'
	done
}

myProg(){
	cd '/home/deva/computer_architecture/assignment6/supporting_files2/'

	l1Config='configL.xml'
	l1Processor='ProcessorL.java'
	configFile='src/configuration/config.xml' 
	processorFile='src/processor/Processor.java'
	pythonTestFile='test_zip.py'
	srcFile='src'
	srcZip='src.zip'
	tempFile='temp'
	statFile='statistic.csv'

	rm -f $statFile

	pwd

	CacheSizeList=(4 8 32 128 1000)
	LatencyList=(1 2 4 8 12)


	ldLatency=${LatencyList[4]}
	ldCacheSize=${CacheSizeList[4]}
	
	echo "for l1d: cache size is "$ldCacheSize" and latency is "$ldLatency
	echo
	echo 'programs,nCycles,nDynamicIns,li latency,li CacheSize,ld latency,ld cacheSize' >> $statFile
	echo '' >> $statFile

	for i in 0 1 2 3 4
	do
		rm -f $configFile
		rm -f $processorFile
		rm -f 'src/processor/Processor.class'
		rm -f $srcZip

		liLatency=${LatencyList[$i]}
		liCacheSize=${CacheSizeList[$i]}

		echo "cache size is "$liCacheSize" and latency is "$liLatency;


		sed "s/l1dLatencyDeva/$ldLatency/g" $l1Config >> $tempFile
		sed "s/l1iLatencyDeva/$liLatency/g" $tempFile >> $configFile
		rm -f $tempFile

		sed "s/l1dCacheSizeDeva/$ldCacheSize/g" $l1Processor >> $tempFile
		sed "s/l1iCacheSizeDeva/$liCacheSize/g" $tempFile >> $processorFile
		rm -f $tempFile

		zip -r -q $srcZip $srcFile

		python $pythonTestFile $srcZip
		echo "getting statistics"
		getStat
		echo "-------------------------------------------------------------"
		echo
	done

	echo
	echo "*****************************************************************"
	
	liLatency=${LatencyList[4]}
	liCacheSize=${CacheSizeList[4]}

	echo "for l1i: cache size is "$liCacheSize" and latency is "$liLatency
	echo
	

	for i in 0 1 2 3 4
	do
		rm -f $configFile
		rm -f $processorFile
		rm -f 'src/processor/Processor.class'
		rm -f $srcZip

		ldLatency=${LatencyList[$i]}
		ldCacheSize=${CacheSizeList[$i]}

		echo "cache size is "$ldCacheSize" and latency is "$ldLatency;


		sed "s/l1dLatencyDeva/$ldLatency/g" $l1Config >> $tempFile
		sed "s/l1iLatencyDeva/$liLatency/g" $tempFile >> $configFile
		rm -f $tempFile

		sed "s/l1dCacheSizeDeva/$ldCacheSize/g" $l1Processor >> $tempFile
		sed "s/l1iCacheSizeDeva/$liCacheSize/g" $tempFile >> $processorFile
		rm -f $tempFile

		zip -r -q $srcZip $srcFile

		python $pythonTestFile $srcZip
		echo "getting statistics"
		getStat
		echo "-------------------------------------------------------------"
		echo
	done
}

myProg