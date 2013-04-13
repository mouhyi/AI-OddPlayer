depth=2
num_games=200

for i in 1 2 3 4
do
	./run.sh $depth $1 $2 $3 $num_games history_tdmc_$i.txt 2 tdmc_$i.txt
done	

./run.sh $depth $1 $2 $3 $num_games history_tdmc_1_vs_tdmc_2.txt \
	2 tdmc_1_v1.1.txt tdmc_1.txt 2 tdmc_2_v1.1.txt tdmc_2.txt
	
./run.sh $depth $1 $2 $3 $num_games history_tdmc_1_vs_tdmc_3.txt \
	2 tdmc_1_v1.2.txt tdmc_1_v1.1.txt 2 tdmc_3_v1.1.txt tdmc_3.txt
	
./run.sh $depth $1 $2 $3 $num_games history_tdmc_1_vs_tdmc_4.txt \
	2 tdmc_1_v1.3.txt tdmc_1_v1.2.txt 2 tdmc_4_v1.1.txt tdmc_4.txt
	
./run.sh $depth $1 $2 $3 $num_games history_tdmc_2_vs_tdmc_3.txt \
	2 tdmc_2_v1.2.txt tdmc_2_v1.1.txt 2 tdmc_3_v1.2.txt tdmc_3_v1.1.txt
	
./run.sh $depth $1 $2 $3 $num_games history_tdmc_2_vs_tdmc_4.txt \
	2 tdmc_2_v1.3.txt tdmc_2_v1.2.txt 2 tdmc_4_v1.2.txt tdmc_4_v1.1.txt
	
./run.sh $depth $1 $2 $3 $num_games history_tdmc_3_vs_tdmc_4.txt \
	2 tdmc_3_v1.3.txt tdmc_3_v1.2.txt 2 tdmc_4_v1.3.txt tdmc_4_v1.2.txt
	
