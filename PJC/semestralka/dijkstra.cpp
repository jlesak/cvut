//
// Created by Jan on 01.12.2017.
//

#include "dijkstra.hpp"

#include <utility>
#include <thread>
#include <iostream>
#include <mutex>

vertex* dijkstra::getMin(std::vector<vertex*> vertexList) {
    vertex* outputVertex = vertexList[0];
    float minimum = std::numeric_limits<float>::infinity();
    for (vertex* v : vertexList){
        if (v->minDistance < minimum){
            outputVertex = v;
            minimum = v->minDistance;
        }
    }
    return outputVertex;
}

vertex* dijkstra::findVertex(int id) {
    for (vertex *v:this->vertexes){
        if (v->id == id) return v;
    }
    return nullptr;
}

std::vector<vertex *> dijkstra::getVertexes() const {
    return vertexes;
}

std::vector<edge *> dijkstra::getEdges() const {
    return edges;
}

void dijkstra::reset() {
    for (vertex* v : vertexes){
        v->minDistance = std::numeric_limits<int>::max();
        v->previousVertex  = nullptr;
    }

}

void dijkstra::createGraph(std::vector<vertex*> vertexList, std::vector<edge*> edgesToVertexes) {
    this->vertexes = std::move(vertexList);

    for (edge* edge1 : edgesToVertexes){
        //projede vsechny hrany
        vertex* v = findVertex(edge1->sourceId); // najde vrchol ve kterym zacina hrana
        v->edges.push_back(edge1); //tomuhle vrcholu priradi hranu
    }
}

std::vector<vertex *> dijkstra::getShortestPathTo(vertex* target) {
    std::vector<vertex*> outputList;
    vertex* vertex1 = findVertex(target->id);
    while (vertex1->previousVertex != nullptr){
        outputList.insert(outputList.begin(), vertex1);
        vertex1 = vertex1->previousVertex;
    }
    outputList.insert(outputList.begin(), vertex1);
    return outputList;
}

void dijkstra::computePath(vertex *source) {
    std::this_thread::sleep_for(std::chrono::milliseconds(100));
    reset();
    source->minDistance = 0; // delku cesty do toho sameho bodu nastavim na 0

    std::vector<vertex*> nonvisitedVertexes = vertexes;  // nenavstivene vrcholy

    while (!nonvisitedVertexes.empty()) // dokud jsem neprojel vsechny vrcholy
    {
        vertex* vertex1 = getMin(nonvisitedVertexes); // z nonvisited vezmu vektor s nejmensi minDistance
        //odeberu vertex1 z nonvisitedVertexes
        for (int i = 0; i < nonvisitedVertexes.size(); ++i) {
            if (nonvisitedVertexes[i] == vertex1){
                nonvisitedVertexes.erase(nonvisitedVertexes.begin()+i);
                break;
            }
        }

        //projde hrany vrcholu vertex1
        for (edge* edge1 : vertex1->edges){
            vertex* child = findVertex(edge1->targetId);//konec hrany
            int distance = vertex1->minDistance + edge1->weight;
            if(distance < child->minDistance){ //pokud je vzdalenost ze source do potomka mensi nez uz ulozena minDistance, tak ji prepisu
                child->minDistance = distance;
                child->previousVertex = vertex1;
            }
        }
    }
}

edge::edge(int sourceId, int targetId, int weight) {
    this->weight = weight;
    this->targetId = targetId;
    this->sourceId = sourceId;
}

vertex::vertex(int id) {
    this->id = id;
}
