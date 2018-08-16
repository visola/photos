import axios from 'axios';
import { action, computed, observable } from 'mobx';

export default class Collection {
  @observable data = [];

  get baseApi() {
    throw new Error('No base API defined');
  }

  @action
  addOne(datum) {
    this.data.push(datum);
  }

  create(datum) {
    return axios.post(this.baseApi, datum)
      .then((response) => {
        this.addOne(response.data);
        return this;
      });
  }

  fetch() {
    return axios.get(this.baseApi)
      .then((response) => {
        this.setData(response.data);
        return this;
      });
  }

  @computed
  get length() {
    return this.data.length;
  }

  @action
  setData(newData) {
    this.data = newData;
  }
}