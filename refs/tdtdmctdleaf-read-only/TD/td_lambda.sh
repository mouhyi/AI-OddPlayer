depth=2
num_games=1000

for i in 1 2 3 4
do
	./run.sh $depth $1 $2 $3 $num_games history_td_lambda_$i.txt 1 td_lambda_$i.txt
done	

./run.sh $depth $1 $2 $3 $num_games history_td_lambda_1_vs_td_lambda_2.txt \
	1 td_lambda_1_v1.1.txt td_lambda_1.txt 1 td_lambda_2_v1.1.txt td_lambda_2.txt
	
./run.sh $depth $1 $2 $3 $num_games history_td_lambda_1_vs_td_lambda_3.txt \
	1 td_lambda_1_v1.2.txt td_lambda_1_v1.1.txt 1 td_lambda_3_v1.1.txt td_lambda_3.txt
	
./run.sh $depth $1 $2 $3 $num_games history_td_lambda_1_vs_td_lambda_4.txt \
	1 td_lambda_1_v1.3.txt td_lambda_1_v1.2.txt 1 td_lambda_4_v1.1.txt td_lambda_4.txt
	
./run.sh $depth $1 $2 $3 $num_games history_td_lambda_2_vs_td_lambda_3.txt \
	1 td_lambda_2_v1.2.txt td_lambda_2_v1.1.txt 1 td_lambda_3_v1.2.txt td_lambda_3_v1.1.txt
	
./run.sh $depth $1 $2 $3 $num_games history_td_lambda_2_vs_td_lambda_4.txt \
	1 td_lambda_2_v1.3.txt td_lambda_2_v1.2.txt 1 td_lambda_4_v1.2.txt td_lambda_4_v1.1.txt
	
./run.sh $depth $1 $2 $3 $num_games history_td_lambda_3_vs_td_lambda_4.txt \
	1 td_lambda_3_v1.3.txt td_lambda_3_v1.2.txt 1 td_lambda_4_v1.3.txt td_lambda_4_v1.2.txt
	
