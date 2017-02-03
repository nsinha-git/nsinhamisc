#include<stdio.h>
#include<iostream>
#include<memory>
using namespace std;
/*implements the DP skeleton through which classes can derive to use automatic dp capabilityshared pointer eases release semantics of underlying map.*/
template<typename K,typename V>
class dynamic_program
{
public:
	dynamic_program(bool (*compare)(K,K)){dyn_map=make_shared<map<K,V>>();}
	dynamic_program(const dynamic_program &d){ dyn_map=d.dyn_map;}
	void store(K k,V v){ dyn_map->insert(make_pair(k,v));}
	bool retrieve(K k,V &v){auto it=dyn_map->find(k);
				if(it==dyn_map->end()) return false;
				v=it->second;
				return true;
	}	
		
private:
	shared_ptr<map<K,V>> dyn_map;

};
