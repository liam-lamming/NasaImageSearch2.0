package com.example.nasaimagesearch20;

import java.util.List;

public class NASAImageResponse {
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private List<Data> data;
        private List<Link> links;

        // Getter
        public List<Data> getData() {
            return data;
        }

        // Setter
        public void setData(List<Data> data) {
            this.data = data;
        }

        public List<Link> getLinks() {
            return links;
        }

        public void setLinks(List<Link> links) {
            this.links = links;
        }

        public static class Data {
            private String title;

            // Getter
            public String getTitle() {
                return title;
            }

            // Setter
            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class Link {
            private String href;

            // Getter
            public String getHref() {
                return href;
            }

            // Setter
            public void setHref(String href) {
                this.href = href;
            }
        }
    }
}
