#include <iostream>
#include <fstream>
#include <thread>
#include <mutex>
#include <sstream>
#include "dijkstra.hpp"

template <typename TimePoint>
std::chrono::milliseconds to_ms(TimePoint tp) {
    return std::chrono::duration_cast<std::chrono::milliseconds>(tp);
}

void computeForVertexes(std::vector<vertex *> dijkstraVertexes, dijkstra &dijkstra){

    std::ostringstream output;
    for (vertex* vertexToCompute : dijkstraVertexes){
        dijkstra.computePath(vertexToCompute);
        output << "================= ID " << vertexToCompute->id << " =================" << "\n";
        // Print minDitance and path from current vertex to each other
        for (vertex* vertex1 : dijkstra.getVertexes())
        {
            output<<"     To " << " id(" << vertex1->id << ")" << " is: " << vertex1->minDistance << "\n ";
            output<<"     Path is:";

            auto path = dijkstra.getShortestPathTo(vertex1);
            for(vertex* vertexInPath : path){
                output << vertexInPath->id << " ";
            }
            output << '\n';
        }
        output << '\n';
    }
    std::cout << output.str();
}

void singleThread(dijkstra *dijkstra) {
    auto dijkstraVertexes = dijkstra->getVertexes();

    auto start = std::chrono::high_resolution_clock::now();
    computeForVertexes(dijkstraVertexes, *dijkstra);
    auto end = std::chrono::high_resolution_clock::now();
    std::cout << "Needed " << to_ms(end - start).count() << " ms to finish.\n";
}

void multiThread(dijkstra dijkstra){
    auto all_vertexes = dijkstra.getVertexes();
    std::size_t const half_size = all_vertexes.size() / 2;
    std::vector<vertex*> first_half(all_vertexes.begin(), all_vertexes.begin() + half_size);
    std::vector<vertex*> second_half(all_vertexes.begin() + half_size, all_vertexes.end());

    auto start = std::chrono::high_resolution_clock::now();

    std::thread t1(computeForVertexes, first_half, std::ref(dijkstra));
    std::thread t2(computeForVertexes, second_half, std::ref(dijkstra));

    t1.join();
    t2.join();

    auto end = std::chrono::high_resolution_clock::now();
    std::cout << "Needed " << to_ms(end - start).count() << " ms to finish.\n";

}

int main(int argc, char *argv[]) {
    bool multi_thread = false;
    std::cout << "argument: " << argv[1] << "\n";
    if(std::string(argv[1]) == "-help"){
        std::cout << "Pro jednovlaknovou verzi pouzijte prepinac -single" << "\n";
        std::cout << "Pro vicevlaknovou verzi pouzijte prepinac -multi" << "\n";
        return 0;
    }
    else if(std::string(argv[1]) == "-single") multi_thread = false;
    else if(std::string(argv[1]) == "-multi") multi_thread = true;
    else {
        std::cout << "Neplatny prepinac!";
        return 0;
    }

    auto *dijkstra1 = new dijkstra();
    std::vector<vertex*> vertexes;
    std::vector<edge*> edges;

    std::string line;
    std::ifstream file("../graph.txt");
    std::vector<int> vertexLine;
    std::string stringNumber;

    if(file.is_open()){
        //projde vsechny radky (vertexy) a zapise jednotliva cisla do vertexLine
        while (getline(file, line)){
            for (char i : line) {
                if (std::isdigit(i)){
                    stringNumber+=i;
                }
                else {
                    vertexLine.push_back(std::stoi(stringNumber));
                    stringNumber = "";
                }
            }

            vertexes.push_back(new vertex(vertexLine[0]));
            for (int j = 1; j < vertexLine.size(); j++) {
                edges.push_back(new edge(vertexLine[0], vertexLine[j], vertexLine[++j]));
            }
            vertexLine.clear();
        }
    }
    file.close();

    dijkstra1->createGraph(vertexes, edges);
    if(multi_thread){
        multiThread(*dijkstra1);
    }
    else singleThread(dijkstra1);
    delete dijkstra1;
}

