import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AlignmentType, Document, Packer, Paragraph, TabStopPosition, TabStopType, TextRun } from 'docx';

import { saveAs } from 'file-saver';

import { IVisit } from 'app/shared/model/visit.model';

@Component({
  selector: 'jhi-visit-detail',
  templateUrl: './visit-detail.component.html',
})
export class VisitDetailComponent implements OnInit {
  visit: IVisit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visit }) => (this.visit = visit));
  }

  previousState(): void {
    window.history.back();
  }

  creatingAndDownloadingPDF(): void {
    const doc = new Document();

    doc.addSection({
      children: [
        new Paragraph({
          alignment: AlignmentType.CENTER,
          spacing: {
            after: 50,
          },
          children: [
            new TextRun({
              text: 'Обласная клиническая больница им. И.И. Мечникова',
              size: 30,
              bold: true,
            }),
          ],
        }),
        new Paragraph({
          tabStops: [
            {
              type: TabStopType.RIGHT,
              position: TabStopPosition.MAX,
            },
          ],
          children: [
            new TextRun({
              text: 'Тел 0988944992, 0991582275',
            }),
            new TextRun({
              text: '\tУкраина, 49005',
            }),
          ],
        }),
        new Paragraph({
          tabStops: [
            {
              type: TabStopType.RIGHT,
              position: TabStopPosition.MAX,
            },
          ],
          children: [
            new TextRun({
              text: 'Факс: (0562) 36-09-67',
            }),
            new TextRun({
              text: '\tг. Днепр',
            }),
          ],
        }),
        new Paragraph({
          spacing: {
            after: 50,
          },
          tabStops: [
            {
              type: TabStopType.RIGHT,
              position: TabStopPosition.MAX,
            },
          ],
          children: [
            new TextRun({
              text: 'e-mail: psookbm@gmail.com',
            }),
            new TextRun({
              text: '\tпл. Соборная, 14',
            }),
          ],
        }),
        new Paragraph({
          text: 'Консультировал врач ' + this.visit!.user!.lastName + ' ' + this.visit!.user!.firstName,
          alignment: AlignmentType.CENTER,
          spacing: {
            after: 50,
          },
        }),
        new Paragraph({
          text: 'Б-ой: ' + this.visit!.patient!.fullName,
          spacing: {
            after: 50,
          },
        }),
        new Paragraph({
          text: 'Прож: ' + this.visit!.patient!.address,
          spacing: {
            after: 50,
          },
        }),
        new Paragraph({
          text: 'Д-з: ' + this.visit!.patient!.diagnosis,
          spacing: {
            after: 50,
          },
        }),
        new Paragraph({
          text: 'Рекомендовано: ' + this.visit!.therapy,
          spacing: {
            after: 50,
          },
        }),
        new Paragraph({
          text: new Date().getDate().toString() + '.' + (new Date().getMonth() + 1).toString() + '.' + new Date().getFullYear().toString(),
          spacing: {
            after: 50,
          },
        }),
      ],
    });

    Packer.toBlob(doc).then(blob => {
      saveAs(blob, this.visit!.patient!.fullName + '.docx');
    });
  }
}
