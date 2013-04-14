// Game of Othello -- Cut from full code
// Universidad Simon Bolivar, 2005.
// Author: Blai Bonet
// Last Revision: 05/17/06
// Modified by: Cristian Caroli
//              Luis Serrano
//              Ignacio Morales

#include <iostream>
#include <iomanip>
#include "assert.h"
#include "othello_cut.h"


unsigned
state_t::value() const
{
  int v = 0;
  for( int pos = 0; pos < DIM; ++pos ) {
    if( !is_free(pos) ) v += (is_black(pos)?-1:1);
  }
  assert( (-36 <= v) && (v <= 36) );
  return(36+v);
}

bool
state_t::terminal( void ) const
{
  if( is_full() ) return(true);
  for( unsigned b = 0; b < DIM; ++b )
    if( is_black_move(b) || is_white_move(b) ) return(false);
  return(true);
}

bool
state_t::outflank( bool color, int pos ) const
{
  if( !is_free(pos) ) return(false);

  const int *p = 0;

  // check rows
  const int *r = rows[pos-4];
  while( *r != pos ) ++r;
  if( *(r+1) != -1 ) {
    for( p = r+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > r+1) && (*p != -1) && !is_free(*p) ) return(true);
  }
  if( r != rows[pos-4] ) {
    for( p = r-1; (p >= rows[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < r-1) && (p >= rows[pos-4]) && !is_free(*p) ) return(true);
  }

  // check cols
  const int *c = cols[pos-4];
  while( *c != pos ) ++c;
  if( *(c+1) != -1 ) {
    for( p = c+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > c+1) && (*p != -1) && !is_free(*p) ) return(true);
  }
  if( c != cols[pos-4] ) {
    for( p = c-1; (p >= cols[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < c-1) && (p >= cols[pos-4]) && !is_free(*p) ) return(true);
  }

  //check diags1 "\"
  const int *d1 = dia1[pos-4];
  while( *d1 != pos ) ++d1;

  if( *(d1+1) != -1 ) {
    for( p = d1+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > d1+1) && (*p != -1) && !is_free(*p) ) return(true);
  }
  if( d1 != dia1[pos-4] ) {
    for( p = d1-1; (p >= dia1[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < d1-1) && (p >= dia1[pos-4]) && !is_free(*p) ) return(true);
  }

  //check diags2 "/"
  const int *d2 = dia2[pos-4];
  while( *d2 != pos ) ++d2;

  if( *(d2+1) != -1 ) {
    for( p = d2+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > d2+1) && (*p != -1) && !is_free(*p) ) return(true);
  }
  if( d2 != dia2[pos-4] ) {
    for( p = d2-1; (p >= dia2[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < d2-1) && (p >= dia2[pos-4]) && !is_free(*p) ) return(true);
  }

  return(false);
}

inline void
state_t::set_color( bool color, int pos )
{
  if( color ) {
    if( pos < 4 )
      t_ |= (1<<pos);
    else {
      free_ |= (1<<pos-4);
      pos_ |= (1<<pos-4);
    }
  }
  else {
    if( pos < 4 )
      t_ &= ~(1<<pos);
    else {
      free_ |= (1<<pos-4);
      pos_ &= ~(1<<pos-4);
    }
  }
}

state_t
state_t::move( bool color, int pos ) const
{
  state_t s(*this);
  if( pos == DIM ) return(s);

  assert( outflank(color,pos) );
  s.set_color(color,pos);

  // process rows
  const int *p = 0;
  const int *r = rows[pos-4];
  while( *r != pos ) ++r;
  if( *(r+1) != -1 ) {
    for( p = r+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > r+1) && (*p != -1) && !is_free(*p) )
      for( const int *q = r+1; q < p; ++q ) s.set_color(color,*q);
  }
  if( r != rows[pos-4] ) {
    for( p = r-1; (p >= rows[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < r-1) && (p >= rows[pos-4]) && !is_free(*p) )
      for( const int *q = r-1; q > p; --q ) s.set_color(color,*q);
  }

  // process columns
  const int *c = cols[pos-4];
  while( *c != pos ) ++c;
  if( *(c+1) != -1 ) {
    for( p = c+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > c+1) && (*p != -1) && !is_free(*p) )
      for( const int *q = c+1; q < p; ++q ) s.set_color(color,*q);
  }
  if( c != cols[pos-4] ) {
    for( p = c-1; (p >= cols[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < c-1) && (p >= cols[pos-4]) && !is_free(*p) )
      for( const int *q = c-1; q > p; --q ) s.set_color(color,*q);
  }

  //process diags1 "\"
  const int *d1 = dia1[pos-4];
  while( *d1 != pos ) ++d1;
  if( *(d1+1) != -1 ) {
    for( p = d1+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > d1+1) && (*p != -1) && !is_free(*p) )
      for( const int *q = d1+1; q < p; ++q ) s.set_color(color,*q);
  }
  if( d1 != dia1[pos-4] ) {
    for( p = d1-1; (p >= dia1[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < d1-1) && (p >= dia1[pos-4]) && !is_free(*p) )
      for( const int *q = d1-1; q > p; --q ) s.set_color(color,*q);
  }


  //process diags2 "/"
  const int *d2 = dia2[pos-4];
  while( *d2 != pos ) ++d2;
  if( *(d2+1) != -1 ) {
    for( p = d2+1; (*p != -1) && !is_free(*p) && (color^is_black(*p)); ++p );
    if( (p > d2+1) && (*p != -1) && !is_free(*p) )
      for( const int *q = d2+1; q < p; ++q ) s.set_color(color,*q);
  }
  if( d2 != dia2[pos-4] ) {
    for( p = d2-1; (p >= dia2[pos-4]) && !is_free(*p) && (color^is_black(*p)); --p );
    if( (p < d2-1) && (p >= dia2[pos-4]) && !is_free(*p) )
      for( const int *q = d2-1; q > p; --q ) s.set_color(color,*q);
  }

  return(s);
}

void
state_t::print( std::ostream &os, int depth ) const
{
  os << "+";
  for( int j = 0; j < N; ++j ) os << "-";
  os << "+" << std::endl;

  int pos = 4;
  for( int i = 0; i < N; ++i ) {
    os << "|";
    for( int j = 0; j < N; ++j )
      if( ((i != 2) && (i != 3)) || ((j != 2) && (j != 3)) ) {
        os << (is_free(pos)?' ':(is_black(pos)?'&':'O'));
        ++pos;
      }
      else {
        assert( ((i == 2) || (i == 3)) && ((j == 2) || (j == 3)) );
        int p = ((i-2)<<1) + (j-2);
        os << (is_free(p)?' ':(is_black(p)?'&':'O'));
      }
    os << "|" << std::endl;
  }

  os << "+";
  for( int j = 0; j < N; ++j ) os << "-";
  os << "+" << std::endl;
}

void
state_t::print_bits( std::ostream &os ) const
{
  for( int i = 3; i >= 0; --i ) os << (t_&(1<<i)?'1':'0');
  os << ":";
  for( int i = 31; i >= 0; --i ) os << (pos_&(1<<i)?'1':'0');
  os << ":";
  for( int i = 31; i >= 0; --i ) os << (free_&(1<<i)?'1':'0');
}
