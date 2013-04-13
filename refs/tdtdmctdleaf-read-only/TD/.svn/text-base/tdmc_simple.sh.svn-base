depth=2
num_games=200

for i in 1 2 3 4
do
	./run.sh $depth $1 $2 $3 $num_games history_tdmc_simple_$i.txt 3 tdmc_simple_$i.txt
done	

./run.sh $depth $1 $2 $3 $num_games history_tdmc_simple_1_vs_tdmc_simple_2.txt \
	3 tdmc_simple_1_v1.1.txt tdmc_simple_1.txt 3 tdmc_simple_2_v1.1.txt tdmc_simple_2.txt
	
./run.sh $depth $1 $2 $3 $num_games history_tdmc_simple_1_vs_tdmc_simple_3.txt \
	3 tdmc_simple_1_v1.2.txt tdmc_simple_1_v1.1.txt 3 tdmc_simple_3_v1.1.txt tdmc_simple_3.txt
	
./run.sh $depth $1 $2 $3 $num_games history_tdmc_simple_1_vs_tdmc_simple_4.txt \
	3 tdmc_simple_1_v1.3.txt tdmc_simple_1_v1.2.txt 3 tdmc_simple_4_v1.1.txt tdmc_simple_4.txt
	
./run.sh $depth $1 $2 $3 $num_games history_tdmc_simple_2_vs_tdmc_simple_3.txt \
	3 tdmc_simple_2_v1.2.txt tdmc_simple_2_v1.1.txt 3 tdmc_simple_3_v1.2.txt tdmc_simple_3_v1.1.txt
	
./run.sh $depth $1 $2 $3 $num_games history_tdmc_simple_2_vs_tdmc_simple_4.txt \
	3 tdmc_simple_2_v1.3.txt tdmc_simple_2_v1.2.txt 3 tdmc_simple_4_v1.2.txt tdmc_simple_4_v1.1.txt
	
./run.sh $depth $1 $2 $3 $num_games history_tdmc_simple_3_vs_tdmc_simple_4.txt \
	3 tdmc_simple_3_v1.3.txt tdmc_simple_3_v1.2.txt 3 tdmc_simple_4_v1.3.txt tdmc_simple_4_v1.2.txt
	
