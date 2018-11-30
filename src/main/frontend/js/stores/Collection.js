import axios from 'axios';
import { action, computed, observable } from 'mobx';

export default class Collection {
  @observable data = [];
  @observable loading = false;
  @observable saving = false;

  get baseApi() {
    throw new Error('No base API defined');
  }

  @action
  addOne(datum) {
    this.data.push(datum);
  }

  @action
  create(datum) {
    this.saving = true;
    return axios.post(this.baseApi, datum)
      .then((response) => {
        this.addOne(response.data);
        this.saving = false;
        return this;
      });
  }

  filter(callback) {
    return this.data.filter(callback);
  }

  fetch() {
    this.loading = true;
    return axios.get(this.baseApi)
      .then((response) => {
        this.setData(response.data);
        this.loading = false;
        return this;
      });
  }

  findById(id) {
    return this.data.find((d) => d.id == id);
  }

  @computed
  get length() {
    return this.data.length;
  }

  @computed
  get isEmpty() {
    return this.length === 0;
  }

  map(callback) {
    return this.data.map(callback);
  }

  @action
  update(datum) {
    this.saving = true;
    return axios.put(`${this.baseApi}/${datum.id}`, datum)
      .then(({data}) => {
        const indexOf = this.data.findIndex(d => d.id === data.id);

        const newArray = this.data;
        newArray[indexOf] = data;

        this.setData(newArray);
        this.saving = false;
        return this;
      });
  }

  @action
  saveOne(datum) {
    if (datum.id == null) {
      return this.create(datum);
    }
    return this.update(datum);
  }

  @action
  setData(newData) {
    this.data = newData;
  }
}