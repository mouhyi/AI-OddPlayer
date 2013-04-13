#include <iomanip>
#include <ext/hash_map>
#include <limits.h>
#include <ctime>
#include <algorithm>
#include "othello_cut.h"



#define MINNODE 0
#define MAXNODE 1

//Variables Globales

bool myColor;
bool otColor;
int expandidos;


////////////////////HASH////////////////////
//Estructura para manejar el hash
class alphaBeta {
    public:
    int f_minus, f_plus;
	explicit alphaBeta(): f_minus(INT_MIN), f_plus(INT_MAX) {}
};

namespace __gnu_cxx {
    template<> class hash<state_t> {
    public:
        size_t operator()( const state_t &s ) const { return(s.free() ^ s.pos()); }
    };
};

class hash_t : public __gnu_cxx::hash_map<state_t,alphaBeta> { };  // class
hash_t hash; // hash instance

////////////////////////////////////////////

state_t* firstchild (state_t node, int nodeType, int &p){

	bool color;
	state_t * rNode= (state_t *) malloc (sizeof(state_t));

	//Determinamos el color

	if (nodeType==MAXNODE){
		color = myColor;
	} else {
		color = otColor;
	}	


	//Busca posicion para colocar ficha de color
    for (int i= 0; i<DIM; i++){
		
		if (node.outflank(color,i)){
			p= i;
			(*rNode)= node.move(color,p);
			return rNode;
		}
	}

    p= 36;
    (*rNode)= node.move(color,36);
	//Si no encuentra posicion
	return rNode;
}

state_t* nextchild (state_t node, int nodeType, int &p){

	bool color;
	state_t * rNode= (state_t *) malloc (sizeof(state_t));;

	//Determinamos el color
	if (nodeType==MAXNODE){
		color = myColor;
	} else {
		color = otColor;
	}	


	//Busca posicion para colocar ficha de color
    for (int i= p+1; i<DIM; i++){
		
		if (node.outflank(color,i)){
			p= i;
			(*rNode) = node.move(color,p);
			return rNode;
		}
	}

	//Si no encuentra posicion
	free(rNode);
	return NULL;
}


int alphabeta(state_t node, char alpha, char beta, int nodeType, int &bp) {

//node.print(std::cout,36);


	char g;
	char a;
	char b;
	state_t * c;
	int p = 0;
	alphaBeta ab;
	char arg;
	char x;

    //nodo expandido
    expandidos++;
    //Se busca el nodo en la tabla de hash
    hash_t::iterator it= hash.find(node);

    //Si el nodo esta en la tabla
    if(it!=hash.end()){
		if(((*it).second.f_minus) >= beta)
			return ((*it).second.f_minus);
	    
		if(((*it).second.f_plus) <= alpha)
			return ((*it).second.f_plus);

    }

	if (node.terminal()) {
		g = (char) node.value();

	} else {

		if (nodeType==MAXNODE){

			g= 0;
			a= alpha;

			c = firstchild(node,nodeType,p);
			while ((g<beta) && (c!=NULL)) {
                int temp = (int)x;
				arg= alphabeta((*c),a,beta,MINNODE,temp);
                if (arg>g) bp=p;
				g= std::max(g,arg);
				a= std::max(a,g);
				free(c);
				c= nextchild(node,nodeType,p);
			}

		} else {

			g= 127;
			b= beta;

			c= firstchild(node,nodeType,p);		
			while ((g>alpha) && (c!=NULL)) {
                int temp = (int)x;
				arg= alphabeta((*c),alpha,b,MAXNODE,temp);
				if (arg<g) bp=p;
				g= std::min(g,arg);
				b= std::min(b,g);
				free(c);
				c= nextchild(node,nodeType,p);
			}
		}
	}

	//UPDATEAR HASH AQUI?

	if(it!=hash.end()){

		if (g<beta)
			(*it).second.f_plus= g;
		if (g>alpha)
			(*it).second.f_minus= g;
	} else {

		if (g<beta)
			ab.f_plus= g;
		if (g>alpha)
			ab.f_minus= g;
		hash.insert(std::make_pair(node,ab));
	}

	return g;
}


char MT_SSS (state_t node, int player){
    
	char g = 127;
	char y= 0;
	int bestPlay;
	clock_t start,finish;
	double time;
    start = clock();
    while (y!=g){
		y= g;
		g= alphabeta(node,(y-1),y,player,bestPlay);
	}
	
	finish = clock();
    time = (double(finish)-double(start))/CLOCKS_PER_SEC;;

	std::cout<<"g: "<<(int)g<<" expandidos:"<<expandidos<<" tiempo: "<<time<<std::endl;
	return g;
}


int MT_BIN (state_t node, int player){

    char g = 200;
    char y;
    int bestPlay;
    char alpha=0;
    char beta=72;

    clock_t start,finish;
	double time;
    start = clock();
       
       do{
          g=(beta+alpha)/2;
          y=g;
          
          g=alphabeta(node,g,g+1,player, bestPlay);
          
          if(g <= y){
             beta=g;
          }else if(g >= (y+1)){
             alpha=g;
          }          
       }while(alpha!=beta);
       
    finish = clock();
    time = (double(finish)-double(start))/CLOCKS_PER_SEC;;
    std::cout<<"g: "<<(int)g<<" expandidos:"<<expandidos<<" tiempo: "<<time<<std::endl;
    return g;
}


int main(int argc, char* argv[]) {
    
    //yo juego los impares!
	myColor = false;
	otColor = true;
	
	expandidos=0;
	
	bool player = true;
	state_t prueba;
	
	//define q jugada se va a evaluar (de 1 a 32)
	int cota=atoi(argv[2]);
	
	//define si es es MT_SSS o MT_BINARIO (1 o 2)
	int tipo = atoi(argv[1]);

    for (int i =0; i<cota; i++){

        prueba = prueba.move(player, PV[i]);
        player= !player;       
    }

    if(tipo==1){ 
        if (player)MT_SSS(prueba,MINNODE);
        else MT_SSS(prueba,MAXNODE);
    }else if(tipo==2){ 
        if (player)MT_BIN(prueba,MINNODE);
        else MT_BIN(prueba,MAXNODE);
    }
    else std::cout<<"no ta aca"<<std::endl;
}

