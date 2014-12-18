#ifndef GENERATOR_H
#define GENERATOR_H

#include <list>
#include <stack>

#define NUMBER_OF_SQUARES 50

class Direction
{
public:
    Direction * left;
    Direction * right;

    int neighbourIndex;
};

class Square
{
public:
    Square();

    int number;
	bool empty;
	bool white; // only use when empty=false
	bool king;  // only use when empty=false

    void init(Square * neighbour0, Square * neighbour1, Square * neighbour2, Square * neighbour3);
	Square * to(Direction &direction);

private:
	Square * neighbour [4];
};

class Generator
{
public:
    Generator();
    std::list< std::list<int> > * generatePaths(Square square []);

private:
	Square * square;

    Direction leftForWhite;
    Direction rightForWhite;
    Direction leftForBlack;
    Direction rightForBlack;

	std::list<Square*> path;
	int length;

    std::list< std::list<int> > paths;

	void generatePathsForWhite();
	void generatePathsForBlack();
	void generatePathsForMan(Direction& direction);
	void generatePathsForKing(Direction& direction);
	void generatePathsForKing2(Direction& direction);
	void addPath();
};

#endif