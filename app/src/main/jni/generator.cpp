#include <android/log.h>
#include <algorithm>
#include "generator.h"

Square::Square()
{

}

void Square::init(Square * neighbour0, Square * neighbour1, Square * neighbour2, Square * neighbour3)
{
    neighbour[0] = neighbour0;
    neighbour[1] = neighbour1;
    neighbour[2] = neighbour2;
    neighbour[3] = neighbour3;
}

Square * Square::to(Direction& direction)
{
    return neighbour[direction.neighbourIndex];
}

Generator::Generator() 
{
    leftForWhite.neighbourIndex = 0;
    leftForWhite.left = &rightForBlack;
    leftForWhite.right = &rightForWhite;

    rightForWhite.neighbourIndex = 1;
    rightForWhite.left = &leftForWhite;
    rightForWhite.right = &leftForBlack;
    
    leftForBlack.neighbourIndex = 2;
    leftForBlack.left = &rightForWhite;
    leftForBlack.right = &rightForBlack;

    rightForBlack.neighbourIndex = 3;
    rightForBlack.left = &leftForBlack;
    rightForBlack.right = &leftForWhite;
}

std::list< std::list<int> > * Generator::generatePaths(Square sq [])
{
    square = sq;

	paths.clear();
	length = 2;

	if (square[0].white)
	{
		generatePathsForWhite();
	}
	else
	{
		generatePathsForBlack();
	}

    return &paths;
}

void Generator::generatePathsForWhite()
{
	for (int number = 1; number <= NUMBER_OF_SQUARES; number++)
	{
	    Square& s = square[number];
		if (!s.empty && s.white)
		{
			path.push_back(&s);
			s.empty = true;
			if (s.king)
			{
				generatePathsForKing(leftForWhite);
			    generatePathsForKing(rightForWhite);
				generatePathsForKing(leftForBlack);
				generatePathsForKing(rightForBlack);
			}
			else
			{
				generatePathsForMan(leftForWhite);
				generatePathsForMan(rightForWhite);
			}
			s.empty = false;
			path.pop_back();
		}
	}
}

void Generator::generatePathsForBlack()
{
	for (int number = NUMBER_OF_SQUARES; number >= 1; number--)
	{
	    Square& s = square[number];
		if (!s.empty && !s.white)
		{
			path.push_back(&s);
			s.empty = true;
			if (s.king)
			{
				generatePathsForKing(leftForBlack);
				generatePathsForKing(rightForBlack);
				generatePathsForKing(leftForWhite);
				generatePathsForKing(rightForWhite);
			}
			else
			{
				generatePathsForMan(leftForBlack);
				generatePathsForMan(rightForBlack);
			}
			s.empty = false;
			path.pop_back();
		}
	}
}

void Generator::generatePathsForMan(Direction& direction)
{
	Square * s = path.back()->to(direction);
	path.push_back(s);

	if (s->empty)
	{
		if (path.size() == 2)
		{
			addPath();
		}
	}
	else
	{
		if (s->white != square[0].white)
		{
			s->white = !s->white;
			Square& s2 = * s->to(direction);
			if (s2.empty)
			{
				path.push_back(&s2);
				generatePathsForMan(* direction.left);
				generatePathsForMan(direction);
				generatePathsForMan(* direction.right);
				addPath();
				path.pop_back();
			}
			s->white = !s->white;
		}
	}
	path.pop_back();
}

void Generator::generatePathsForKing(Direction& direction)
{
	Square * s = path.back()->to(direction);
	path.push_back(s);

	while (s->empty)
	{
        if (path.size() == 2) {
            addPath();
        }

		path.pop_back();
		s = s->to(direction);
		path.push_back(s);
	}

	if (s->white != square[0].white)
	{
		s->white = !s->white;
		Square& s2 = * s->to(direction);
		if (s2.empty)
		{
			generatePathsForKing2(direction);
		}
		s->white = !s->white;
	}

	path.pop_back();
}

void Generator::generatePathsForKing2(Direction& direction)
{
	Square * s = path.back()->to(direction);
	path.push_back(s);

	while (s->empty)
	{
    	generatePathsForKing(* direction.left);
		generatePathsForKing(* direction.right);
        addPath();

		path.pop_back();
		s = s->to(direction);
		path.push_back(s);
	}

	if (s->white != square[0].white)
	{
		s->white = !s->white;
		Square& s2 = * s->to(direction);
		if (s2.empty)
		{
			generatePathsForKing2(direction);
		}
		s->white = !s->white;
	}

	path.pop_back();
}

void Generator::addPath()
{
	if (path.size() > length)
	{
        paths.clear();
		length = path.size();
	}
	if (path.size() == length)
	{
	    std::list<int> numbers (path.size());
        transform(path.begin(), path.end(), numbers.begin(), [](Square * s) {return s->number;} );

        paths.push_back(numbers);
	}
}