depth=2
num_games=1000

for i in 1 2 3 4
do
	./run.sh $depth $1 $2 $3 $num_games history_td_leaf_$i.txt 0 td_leaf_$i.txt
done	

./run.sh $depth $1 $2 $3 $num_games history_td_leaf_1_vs_td_leaf_2.txt \
	0 td_leaf_1_v1.1.txt td_leaf_1.txt 0 td_leaf_2_v1.1.txt td_leaf_2.txt
	
./run.sh $depth $1 $2 $3 $num_games history_td_leaf_1_vs_td_leaf_3.txt \
	0 td_leaf_1_v1.2.txt td_leaf_1_v1.1.txt 0 td_leaf_3_v1.1.txt td_leaf_3.txt
	
./run.sh $depth $1 $2 $3 $num_games history_td_leaf_1_vs_td_leaf_4.txt \
	0 td_leaf_1_v1.3.txt td_leaf_1_v1.2.txt 0 td_leaf_4_v1.1.txt td_leaf_4.txt
	
./run.sh $depth $1 $2 $3 $num_games history_td_leaf_2_vs_td_leaf_3.txt \
	0 td_leaf_2_v1.2.txt td_leaf_2_v1.1.txt 0 td_leaf_3_v1.2.txt td_leaf_3_v1.1.txt
	
./run.sh $depth $1 $2 $3 $num_games history_td_leaf_2_vs_td_leaf_4.txt \
	0 td_leaf_2_v1.3.txt td_leaf_2_v1.2.txt 0 td_leaf_4_v1.2.txt td_leaf_4_v1.1.txt
	
./run.sh $depth $1 $2 $3 $num_games history_td_leaf_3_vs_td_leaf_4.txt \
	0 td_leaf_3_v1.3.txt td_leaf_3_v1.2.txt 0 td_leaf_4_v1.3.txt td_leaf_4_v1.2.txt
	
