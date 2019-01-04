#!/bin/bash

mvn compile

if [ -f output.txt ]; then
    echo "removing output.txt"
    rm output.txt
fi

for ((i=1;i<=20;i++));
do
    ./run-benchmark.sh >> output.txt 2>&1
    echo $i
done

echo "average insertion to board (ms):"
grep -w "insertion to board (ms)" output.txt | cut -d ':' -f 2 | \
    awk -v tab="\t" '{sum += $1} END {printf (sum+0)/20 tab}'
echo ""

echo "average board summary (ms):"
grep -w "board summary (ms)" output.txt | cut -d ':' -f 2 | \
    awk -v tab="\t" '{sum += $1} END {printf (sum+0)/20 tab}'
echo ""

echo "Order Type:"
grep -w "concrete order quantity class" output.txt | cut -d ':' -f 2 | \
    awk -v tab="\t" '{sum = $0} END {printf sum tab}'
echo ""

echo "Number Type:"
grep -w "concrete order number class" output.txt | cut -d ':' -f 2 | \
    awk -v tab="\t" '{sum = $0} END {printf sum tab}'
echo ""

echo "Summary Type:"
grep -w "concrete summary quantity class" output.txt | cut -d ':' -f 2 | \
    awk -v tab="\t" '{sum = $0} END {printf sum tab}'
echo ""

echo "Summary Number Type:"
grep -w "concrete summary number class" output.txt | cut -d ':' -f 2 | \
    awk -v tab="\t" '{sum = $0} END {printf sum tab}'
echo ""
