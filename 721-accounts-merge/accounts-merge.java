import java.util.*;

class Solution {
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, Integer> emailToId = new HashMap<>();
        Map<String, String> emailToName = new HashMap<>();

        int id = 0;

        
        for (List<String> account : accounts) {
            String name = account.get(0);

            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);

                if (!emailToId.containsKey(email)) {
                    emailToId.put(email, id++);
                }

                emailToName.put(email, name);
            }
        }

        DSU dsu = new DSU(id);

        
        for (List<String> account : accounts) {
            int first = emailToId.get(account.get(1));

            for (int i = 2; i < account.size(); i++) {
                dsu.union(first, emailToId.get(account.get(i)));
            }
        }

        // Group emails by parent
        Map<Integer, List<String>> groups = new HashMap<>();

        for (String email : emailToId.keySet()) {
            int parent = dsu.find(emailToId.get(email));

            groups.computeIfAbsent(parent, x -> new ArrayList<>()).add(email);
        }

        List<List<String>> result = new ArrayList<>();

        for (List<String> emails : groups.values()) {
            Collections.sort(emails);

            List<String> list = new ArrayList<>();
            list.add(emailToName.get(emails.get(0)));
            list.addAll(emails);

            result.add(list);
        }

        return result;
    }

    class DSU {
        int[] parent;

        DSU(int n) {
            parent = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }

            return parent[x];
        }

        void union(int a, int b) {
            int pa = find(a);
            int pb = find(b);

            if (pa != pb) {
                parent[pb] = pa;
            }
        }
    }
}
